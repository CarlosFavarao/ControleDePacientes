package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.ExamDto;
import br.com.ControleDePacientes.enums.ExamStatus;
import br.com.ControleDePacientes.enums.ExamType;
import br.com.ControleDePacientes.model.DoctorModel;
import br.com.ControleDePacientes.model.ExamModel;
import br.com.ControleDePacientes.model.PatientModel;
import br.com.ControleDePacientes.repository.ExamRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AdmissionLogService admissionLogService;


    @Transactional
    public ExamModel findById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exame não encontrado com id " + id));
    }

    @Transactional
    public ExamDto findByIdDto(Long id) {
        ExamModel exam = findById(id);
        return ExamDto.from(exam);
    }


    @Transactional
    public ExamDto scheduleExam(ExamDto dto) {
        PatientModel patient = patientService.findById(dto.getPatientId());
        DoctorModel doctor = doctorService.findById(dto.getDoctorId());

        validateInternment(patient.getId(), doctor.getId());
        validateDateTime(dto.getDateTime());
        checkConflict(patient, dto.getDateTime());

        ExamModel examModel = createNewExameModel(dto, patient, doctor);
        examModel = examRepository.save(examModel);

        return createNewObjectExamModel(examModel);
    }

    private void validateInternment(Long patientId, Long doctorId){
        boolean isInterned = admissionLogService.existsActiveInternmentByPatientIdAndDoctorId(patientId, doctorId);
        if (!isInterned){
            throw new RuntimeException("Paciente não está internado com esse médico.");
        }
    }

    private void validateDateTime(LocalDateTime dateTime){
        if(dateTime == null || dateTime.isBefore(LocalDateTime.now())){
            throw new RuntimeException("Data e hora devem ser no futuro");
        }
    }

    private void checkConflict(PatientModel patient, LocalDateTime dateTime){
        if(examRepository.existsByPatientAndDateTime(patient, dateTime)){
            throw new RuntimeException("Já existe um exame agendado nesse horário para este paciente.");
        }
    }

    private ExamModel createNewExameModel(ExamDto dto, PatientModel patient, DoctorModel doctor){
        ExamModel model = new ExamModel();
        model.setExamName(dto.getExamName());
        model.setDateTime(dto.getDateTime());
        model.setType(ExamType.valueOf(dto.getType()));
        model.setStatus(ExamStatus.SCHEDULED);
        model.setPatient(patient);
        model.setDoctor(doctor);
        return model;
    }

    private ExamDto createNewObjectExamModel(ExamModel model){
        ExamDto dto = new ExamDto();
        dto.setExamName(model.getExamName());
        dto.setDateTime(model.getDateTime());
        dto.setType(model.getType().name());
        dto.setStatus(model.getStatus().name());
        dto.setNamePatient(model.getPatient().getName());
        dto.setDoctorName(model.getDoctor().getName());
        return dto;
    }

    @Transactional
    public void deleteExam(Long id) {
        ExamModel exam = findById(id);
        examRepository.delete(exam);
    }

    @Transactional
    public List<ExamDto> listExamsByPatients(Long patientId) {
        PatientModel patient = patientService.findById(patientId);

        List<ExamModel> exams = examRepository.findByPatient(patient);

        if (exams.isEmpty()) {
            throw new RuntimeException("O paciente não tem exame agendado.");
        }

        return exams.stream()
                .map(ExamDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExamDto updateExam(Long id, ExamDto dto) {
        ExamModel examModel = findById(id);

        examModel.setExamName(dto.getExamName());
        examModel.setType(validateAndGetType(dto.getType()));
        examModel.setStatus(validateAndGetStatus(dto.getStatus()));
        validateDateTime(dto.getDateTime());

        examModel.setDateTime(dto.getDateTime());
        examModel = examRepository.save(examModel);

        return createNewObjectExamModel(examModel);
    }

    private ExamType validateAndGetType(String type) {
        try {
            return ExamType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de exame inválido: " + type);
        }
    }

    private ExamStatus validateAndGetStatus(String status) {
        try {
            return ExamStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status de exame inválido: " + status);
        }
    }
}


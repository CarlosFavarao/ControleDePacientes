package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.ExamDto;
import br.com.ControleDePacientes.enums.ExamStatus;
import br.com.ControleDePacientes.enums.ExamType;
import br.com.ControleDePacientes.model.DoctorModel;
import br.com.ControleDePacientes.model.ExamModel;
import br.com.ControleDePacientes.model.PatientModel;
import br.com.ControleDePacientes.repository.DoctorRepository;
import br.com.ControleDePacientes.repository.ExamRepository;
import br.com.ControleDePacientes.repository.PatientRepository;
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
        PatientModel patientModel = patientService.findById(dto.getPatientId());
        DoctorModel doctorModel = doctorService.findById(dto.getDoctorId());

        boolean validInternment = admissionLogService.existsActiveInternmentByPatientIdAndDoctorId(
                patientModel.getId(),
                doctorModel.getId()
        );

        if (!validInternment) {
            throw new RuntimeException("Paciente não está internado com esse médico responsável.");
        }

        if (dto.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Data e hora devem ser no futuro.");
        }

        boolean conflito = examRepository.existsByPatientAndDateTime(patientModel, dto.getDateTime());
        if (conflito) {
            throw new RuntimeException("Já existe um exame agendado nesse horário para esse paciente.");
        }

        ExamModel examModel = new ExamModel();
        examModel.setExamName(dto.getExamName());
        examModel.setDateTime(dto.getDateTime());
        examModel.setType(ExamType.valueOf(dto.getType()));
        examModel.setStatus(ExamStatus.SCHEDULED);
        examModel.setPatient(patientModel);
        examModel.setDoctor(doctorModel);

        examModel = examRepository.save(examModel);

        ExamDto resultDto = new ExamDto();
        resultDto.setExamName(examModel.getExamName());
        resultDto.setDateTime(examModel.getDateTime());
        resultDto.setType(examModel.getType().name());
        resultDto.setStatus(examModel.getStatus().name());
        resultDto.setNamePatient(examModel.getPatient().getName());
        resultDto.setDoctorName(examModel.getDoctor().getNameDoctor());

        return resultDto;
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

        try {
            examModel.setType(ExamType.valueOf(dto.getType().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de exame inválido: " + dto.getType());
        }

        try {
            examModel.setStatus(ExamStatus.valueOf(dto.getStatus().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status de exame inválido: " + dto.getStatus());
        }

        if (dto.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Data e hora devem ser no futuro.");
        }
        examModel.setDateTime(dto.getDateTime());

        boolean conflito = examRepository.existsByPatientAndDateTime(examModel.getPatient(), dto.getDateTime());
        if (conflito && !examModel.getDateTime().equals(dto.getDateTime())) {
            throw new RuntimeException("Já existe um exame agendado nesse horário para esse paciente.");
        }

        examModel = examRepository.save(examModel);

        return ExamDto.from(examModel);
    }


}


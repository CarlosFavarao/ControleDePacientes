package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.ExamDto;
import br.com.ControleDePacientes.enums.ExamStatus;
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
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Transactional
    public void ScheduleExam(ExamDto dto) {
        PatientModel patientModel = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

        DoctorModel doctorModel = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doutor não encontrado."));

        if (patientModel.getAdmission() == null) {
            throw new RuntimeException("Paciente não está internado. ");
        }

        if (!patientModel.getResponsibleDoctor().getId().equals(doctorModel.getId())) {
            throw new RuntimeException("Médico solicitante não é o responsável pelo paciente.");
        }
        Long doctorId = patientModel.getResponsibleDoctor().getId();

        if (dto.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Data e hora devem ser no futuro.");
        }

        boolean conflito = examRepository.existsByPatientAndDateTime(patientModel, dto.getDateTime());
        if (conflito) {
            throw new RuntimeException("Já existe um exame agendao nesse horario para esse paciente");
        }

        ExamModel examModel = new ExamModel();
        examModel.setExamName(dto.getExamName());
        examModel.setDateTime(dto.getDateTime());
        examModel.setType(dto.getType());
        examModel.setStatus(ExamStatus.SCHEDULED);
        examModel.setPatient(patientModel);
        examModel.setDoctor(doctorModel);

        examRepository.save(examModel);
    }

    @Transactional
    public void deleteExam(Long id) {
        ExamModel exam = examRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exame não encontrado com id " + id));

        examRepository.delete(exam);
    }

    @Transactional
    public List<ExamDto> listExamsByPatients(Long patientId) {
        PatientModel patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

        List<ExamModel> exam = examRepository.findByPatient(patient);

        if (exam.isEmpty()) {
        throw new RuntimeException("O paciente não tem exame agendado.");
        }

        return exam.stream().map(exams -> {
            ExamDto dto = new ExamDto();
            dto.setExamName(exams.getExamName());
            dto.setDateTime(exams.getDateTime());
            dto.setType(exams.getType());
            dto.setStatus(exams.getStatus());
            dto.setPatientId(exams.getPatient().getId());
            dto.setDoctorId(exams.getDoctor().getId());
            return dto;
        }).collect(Collectors.toList());
    }

}
package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.ExamDto;
import br.com.ControleDePacientes.enums.ExamStatus;
import br.com.ControleDePacientes.model.DoctorModel;
import br.com.ControleDePacientes.model.ExamModel;
import br.com.ControleDePacientes.model.PatientModel;
import br.com.ControleDePacientes.repository.ExamRepository;
import br.com.ControleDePacientes.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;


    public void ScheduleExam(ExamDto dto) {
        PatientModel patientModel = patientRepository.findById(dto.getPaientId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

        DoctorModel doctorModel = doctorRepsiotry.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doutor não encontrado."));

        if (!patientModel.isAdmission()) {
            throw new RuntimeException("Paciente não está internado. ");
        }

        if (!patientModel.getResponsibleDoctor().getId().equals(doctorModel.getId())) {
            throw new RuntimeException("Médico solicitante não é o responsável pelo paciente.");
        }

        if (dto.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Data e hora devem ser no futuro.");
        }

        boolean conflito = examRepository.existsByPatientAndDateTime(patientModel, dto.getDateTime());
        if (conflito) {
            throw new RuntimeException("Já existe um exame agendao nesse horario para esse paciente");
        }

        ExamModel examModel = new ExamModel();
        examModel.setDateTime(dto.getDateTime());
        examModel.setType(dto.getType());
        examModel.setStatus(ExamStatus.ACCOMPLISHED);
        examModel.setPatient(patientModel);
        examModel.setResponsibleDoctor(doctorModel);

        examRepository.save(examModel);
    }
}
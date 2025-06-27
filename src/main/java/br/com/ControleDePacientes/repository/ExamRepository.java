package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.model.ExamModel;
import br.com.ControleDePacientes.model.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ExamRepository extends JpaRepository <ExamModel, Long> {
    boolean existsByPatientAndDateTime(PatientModel patientModel, LocalDateTime dateTime);
}

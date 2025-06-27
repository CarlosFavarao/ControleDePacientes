package br.com.ControleDePacientes.model;

import br.com.ControleDePacientes.enums.ExamStatus;
import br.com.ControleDePacientes.enums.ExamType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ExamModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String examName;
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private ExamType type;

    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    private PatientModel patientModel;

    private DoctorModel doctorModel;
}

package br.com.ControleDePacientes.model;

import br.com.ControleDePacientes.enums.ExamStatus;
import br.com.ControleDePacientes.enums.ExamType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "exam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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

    @ManyToOne(fetch = FetchType.LAZY)
    private PatientModel patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorModel doctor;

}

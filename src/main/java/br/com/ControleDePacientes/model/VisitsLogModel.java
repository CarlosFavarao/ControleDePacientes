package br.com.ControleDePacientes.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "visits_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitsLogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;

    @Column(name = "departure_date", nullable = true)
    private LocalDateTime departureDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_visited_id",nullable = false)
    private PatientModel patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitor_id", nullable = false)
    private VisitorModel visitor;
}

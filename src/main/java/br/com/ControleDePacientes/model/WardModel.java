package br.com.ControleDePacientes.model;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WardModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpecialtyEnum specialty;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalModel hospital;
}

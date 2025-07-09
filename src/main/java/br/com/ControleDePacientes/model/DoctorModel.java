package br.com.ControleDePacientes.model;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class DoctorModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameDoctor;

    @Column(nullable = false, unique = true, length = 50)
    private String crm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpecialtyEnum specialty;
}

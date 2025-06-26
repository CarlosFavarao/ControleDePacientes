package br.com.ControleDePacientes.model;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.LAZY) //Alterei para que não existam buscas infinitas...
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalModel hospital;

    @OneToMany(mappedBy = "ward", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<RoomModel> rooms = new HashSet<>(); //Para usar a tabela hash...
}

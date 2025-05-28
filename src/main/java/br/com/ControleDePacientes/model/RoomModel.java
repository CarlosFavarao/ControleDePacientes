package br.com.ControleDePacientes.model;

import br.com.ControleDePacientes.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter //Optei por começar a usá-los individualmente, O @Data pode ser
@Setter //perigoso em alguns casos pois gera muitos metodos a mais...
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class RoomModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RoomStatus status;

    @ManyToOne
    @JoinColumn(name = "ward_id", nullable = false)
    private WardModel ward;

    //podem haver adições futuras...
}

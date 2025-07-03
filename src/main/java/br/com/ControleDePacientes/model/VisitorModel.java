package br.com.ControleDePacientes.model;

import br.com.ControleDePacientes.validation.ValidDocument;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "visitors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @ValidDocument
    @Column(nullable = false, unique = true)
    private String document;
}
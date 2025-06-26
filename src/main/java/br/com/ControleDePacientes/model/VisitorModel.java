package br.com.ControleDePacientes.model;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String document;
}
package br.com.ControleDePacientes.projections;

import br.com.ControleDePacientes.enums.SpecialtyEnum;

import java.time.LocalDateTime;

public interface AdmissionLogProjection {
    String getName();
    SpecialtyEnum getSpecialty();
    LocalDateTime getAdmissionDate();
    int getDaysAdmitted();
}

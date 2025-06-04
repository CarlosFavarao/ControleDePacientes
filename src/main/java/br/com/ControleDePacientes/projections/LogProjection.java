package br.com.ControleDePacientes.projections;

import br.com.ControleDePacientes.enums.SpecialtyEnum;

import java.time.LocalDateTime;

public interface LogProjection {
    String getName();
    SpecialtyEnum getSpecialty();
    LocalDateTime getAdmissionDate();
    LocalDateTime getDischargeDate();
    int getDaysAdmitted();
}

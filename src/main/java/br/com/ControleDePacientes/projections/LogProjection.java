package br.com.ControleDePacientes.projections;

import br.com.ControleDePacientes.enums.SpecialtyEnum;

import java.time.LocalDateTime;

public interface LogProjection {
    Long getId();
    String getName();
    String getCode();
    SpecialtyEnum getSpecialty();
    LocalDateTime getAdmissionDate();
    LocalDateTime getDischargeDate();
    int getDaysAdmitted();
    String getHospitalName();
}

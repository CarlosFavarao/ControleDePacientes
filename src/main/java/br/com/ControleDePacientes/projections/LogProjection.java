package br.com.ControleDePacientes.projections;

import br.com.ControleDePacientes.enums.LogStatus;
import br.com.ControleDePacientes.enums.SpecialtyEnum;
import br.com.ControleDePacientes.model.BedModel;

import java.time.LocalDateTime;

public interface LogProjection {
    Long getId();
    String getName();
    String getCode();
    SpecialtyEnum getSpecialty();
    LocalDateTime getAdmissionDate();
    LocalDateTime getDischargeDate();
    LogStatus getStatus();
    int getDaysAdmitted();
    String getHospitalName();
    Long getMoved_to();
    Long getDoctorId();
}

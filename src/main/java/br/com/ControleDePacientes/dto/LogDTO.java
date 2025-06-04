package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import br.com.ControleDePacientes.projections.LogProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LogDTO {
    private String patientName;
    private SpecialtyEnum specialty;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;
    private int daysAdmitted;

    public LogDTO(LogProjection logProjection) {
        this.patientName = logProjection.getName();
        this.specialty = logProjection.getSpecialty();
        this.admissionDate = logProjection.getAdmissionDate();
        this.dischargeDate = logProjection.getDischargeDate();
        this.daysAdmitted = logProjection.getDaysAdmitted();
    }
}

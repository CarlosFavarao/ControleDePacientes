package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.LogStatus;
import br.com.ControleDePacientes.enums.SpecialtyEnum;
import br.com.ControleDePacientes.model.BedModel;
import br.com.ControleDePacientes.projections.LogProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LogDTO {
    private Long patientId;
    private String patientName;
    private String roomCode;
    private SpecialtyEnum specialty;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;
    private int daysAdmitted;
    private String hospitalName;
    private LogStatus status;
    private Long moved_to;
    private Long doctorId;

    public LogDTO(LogProjection logProjection) {
        this.patientId = logProjection.getId();
        this.patientName = logProjection.getName();
        this.roomCode = logProjection.getCode();
        this.specialty = logProjection.getSpecialty();
        this.admissionDate = logProjection.getAdmissionDate();
        this.dischargeDate = logProjection.getDischargeDate();
        this.daysAdmitted = logProjection.getDaysAdmitted();
        this.hospitalName = logProjection.getHospitalName();
        this.status = logProjection.getStatus();
        this.moved_to = logProjection.getMoved_to();
        this.doctorId = logProjection.getDoctorId();
    }
}

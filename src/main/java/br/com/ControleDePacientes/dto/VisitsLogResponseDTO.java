package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.model.VisitsLogModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitsLogResponseDTO {
    private Long id;
    private LocalDateTime entryDate;
    private LocalDateTime exitDate;
    private Long patientId;
    private String patientName;
    private Long visitorId;
    private String visitorName;

    public VisitsLogResponseDTO(VisitsLogModel log) {
        this.id = log.getId();
        this.entryDate = log.getEntryDate();
        this.exitDate = log.getExitDate();
        if (log.getPatient() != null) {
            this.patientId = log.getPatient().getId();
            this.patientName = log.getPatient().getName();
        }
        if (log.getVisitor() != null) {
            this.visitorId = log.getVisitor().getId();
            this.visitorName = log.getVisitor().getName();
        }
    }
}

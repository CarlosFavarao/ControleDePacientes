package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.BedType;
import br.com.ControleDePacientes.model.AdmissionLogModel;
import br.com.ControleDePacientes.model.BedModel;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdmissionResponseDTO {
    private Long id;
    private LocalDateTime admissionDate;
    private Long patientId;
    private String patientName;
    private Long bedId;
    private BedModel moved_to;
    private String bedCode;
    private BedType bedType;

    public AdmissionResponseDTO(AdmissionLogModel log) {
        this.id = log.getId();
        this.admissionDate = log.getAdmissionDate();
        if (log.getPatient() != null) {
            this.patientId = log.getPatient().getId();
            this.patientName = log.getPatient().getName();
        }
        if (log.getBed() != null) {
            this.bedId = log.getBed().getId();
            this.bedCode = log.getBed().getCode();
            this.bedType = log.getBed().getBedType();
            this.moved_to = log.getMoved_to();
        }
    }
}
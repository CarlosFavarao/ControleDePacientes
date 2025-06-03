package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.model.AdmissionLogModel;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdmissionResponseDTO {
    private Long id;
    private LocalDateTime admissionDate;
    private Long patientId;
    private String patientName;
    private Long bedId;
    private String bedCode;

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
        }
    }
}
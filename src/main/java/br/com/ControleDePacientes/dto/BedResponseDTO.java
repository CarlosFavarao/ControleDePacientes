package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.model.BedModel;
import lombok.Data;

@Data
public class BedResponseDTO {
    private Long hospitalId;
    private String hospitalName;
    private Long id;
    private String code;
    private BedStatus status;
    private Long roomId;
    private String roomCode;
    private Long patientId;
    private String patientName;

    public BedResponseDTO(BedModel bed) {
        this.hospitalId = bed.getRoom().getWard().getHospital().getId();
        this.hospitalName = bed.getRoom().getWard().getHospital().getName();
        this.id = bed.getId();
        this.code = bed.getCode();
        this.status = bed.getStatus();
        if (bed.getRoom() != null) {
            this.roomId = bed.getRoom().getId();
            this.roomCode = bed.getRoom().getCode();
        }
        if (bed.getPatient() != null) {
            this.patientId = bed.getPatient().getId();
            this.patientName = bed.getPatient().getName();
        }
    }
}

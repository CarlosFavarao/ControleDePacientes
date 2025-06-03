package br.com.ControleDePacientes.dto.beds;

import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.model.BedModel;
import lombok.Data;

@Data
public class BedResponseDTO {
    private Long id;
    private String code;
    private BedStatus status;
    private Long roomId;
    private String roomCode;
    private Long patientId;
    private String patientName;

    public BedResponseDTO(BedModel bed) {
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

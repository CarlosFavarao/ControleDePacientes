package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.RoomStatus;
import br.com.ControleDePacientes.enums.SpecialtyEnum;
import br.com.ControleDePacientes.model.RoomModel;
import lombok.Data;

@Data
public class RoomResponseDTO {
    private Long id;
    private String code;
    private RoomStatus status;
    private Long wardId;
    private SpecialtyEnum wardSpecialty;

    public RoomResponseDTO(RoomModel room) {
        this.id = room.getId();
        this.code = room.getCode();
        this.status = room.getStatus();

        if (room.getWard() != null) {
            this.wardId = room.getWard().getId();
            this.wardSpecialty = room.getWard().getSpecialty();
        }
    }
}

package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AvailableRoomDTO {
    private String specialtyDescription;
    private String roomCode;

    public AvailableRoomDTO(SpecialtyEnum specialty, String roomCode){
        this.specialtyDescription = specialty.getDescription();
        this.roomCode = roomCode;
    }
}

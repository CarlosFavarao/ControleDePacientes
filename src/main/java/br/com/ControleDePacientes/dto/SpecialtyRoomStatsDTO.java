package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialtyRoomStatsDTO {
    private SpecialtyEnum specialty;
    private long totalRooms;
    private long freeRooms;
    private long occupiedRooms;
}

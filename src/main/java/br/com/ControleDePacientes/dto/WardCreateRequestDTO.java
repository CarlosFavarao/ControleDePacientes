package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import lombok.Data;

@Data
public class WardCreateRequestDTO {
    private SpecialtyEnum specialty;
    private Long hospitalId;
    private int numberOfRooms;
    private int bedsPerRoom;
}

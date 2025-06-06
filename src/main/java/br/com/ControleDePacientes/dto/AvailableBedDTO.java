package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.enums.SpecialtyEnum;
import br.com.ControleDePacientes.model.BedModel;
import br.com.ControleDePacientes.model.RoomModel;
import br.com.ControleDePacientes.projections.AvailableBedProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableBedDTO {
    private SpecialtyEnum specialty;
    private Long bedId;
    private String bedCode;
    private BedStatus bedStatus;
    private Long roomId;
    private String roomCode;

    public static AvailableBedDTO fromProjection(AvailableBedProjection projection) {
        return new AvailableBedDTO(
                SpecialtyEnum.valueOf(projection.getSpecialty()),
                projection.getBedId(),
                projection.getBedCode(),
                BedStatus.valueOf(projection.getBedStatus()),
                projection.getRoomId(),
                projection.getRoomCode()
        );
    }
}

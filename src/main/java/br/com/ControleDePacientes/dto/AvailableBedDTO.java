package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import br.com.ControleDePacientes.projections.AvailableBedProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableBedDTO {
    private Long hospitalId;
    private String hospitalName;
    private SpecialtyEnum specialty;
    private Long bedId;
    private String bedCode;
    private Long roomId;
    private String roomCode;

    public static AvailableBedDTO fromProjection(AvailableBedProjection projection) {
        return new AvailableBedDTO(
                projection.getHospitalId(),
                projection.getHospitalName(),
                SpecialtyEnum.valueOf(projection.getSpecialty()),
                projection.getBedId(),
                projection.getBedCode(),
                projection.getRoomId(),
                projection.getRoomCode()
        );
    }
}

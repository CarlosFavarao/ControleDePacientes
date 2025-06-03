package br.com.ControleDePacientes.dto.admission;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import lombok.Data;

@Data
public class AdmissionRequestDTO {
    private Long patientId;
    private SpecialtyEnum specialty;
}

package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import lombok.Data;

@Data
public class AdmissionRequestDTO {
    private Long patientId;
    private Long bedId;
}

package br.com.ControleDePacientes.dto;

import lombok.Data;

@Data
public class AdmissionRequestDTO {
    private Long patientId;
    private Long bedId;
    private Long doctorId;
}

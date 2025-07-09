package br.com.ControleDePacientes.dto;

import lombok.Data;

@Data
public class VisitsLogRequestDTO {
    private Long patientId;
    private Long visitorId;

}

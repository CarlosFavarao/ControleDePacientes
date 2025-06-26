package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientLocationDTO {
    private Long patientId;
    private String patientName;
    private String hospitalName;
    private SpecialtyEnum wardSpecialty;
    private String roomCode;
    private String bedCode;
    private LocalDateTime admissionDate;
}

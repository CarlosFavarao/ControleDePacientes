package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.model.DoctorModel;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferPatientDTO {
    private Long patientId;
    private Long newBedId;
    private DoctorModel doctor;
}

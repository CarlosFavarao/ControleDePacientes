package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import br.com.ControleDePacientes.projections.AdmissionLogProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AdmittedPatientDTO {
    private String patientName;
    private SpecialtyEnum specialty;
    private LocalDateTime admissionDate;
    private int daysAdmitted;

    public AdmittedPatientDTO(AdmissionLogProjection admissionLogProjection) {
        this.patientName = admissionLogProjection.getName();
        this.specialty = admissionLogProjection.getSpecialty();
        this.admissionDate = admissionLogProjection.getAdmissionDate();
        this.daysAdmitted = admissionLogProjection.getDaysAdmitted();
    }
}

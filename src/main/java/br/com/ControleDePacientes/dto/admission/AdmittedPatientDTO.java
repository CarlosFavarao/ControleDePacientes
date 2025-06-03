package br.com.ControleDePacientes.dto.admission;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdmittedPatientDTO {
    private String patientName;
    private SpecialtyEnum specialty;
    private LocalDateTime admissionDate;
    private long daysAdmitted;

    public AdmittedPatientDTO(String patientName, SpecialtyEnum specialty, LocalDateTime admissionDate, long daysAdmitted){
        this.patientName = patientName;
        this.specialty = specialty;
        this.admissionDate = admissionDate;
        this.daysAdmitted = daysAdmitted;
    }
}

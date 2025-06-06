package br.com.ControleDePacientes.projections;

import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.enums.SpecialtyEnum;

public interface AvailableBedProjection {
    Long getHospitalId();
    String getHospitalName();
    String getSpecialty();
    Long getBedId();
    String getBedCode();
    Long getRoomId();
    String getRoomCode();
}

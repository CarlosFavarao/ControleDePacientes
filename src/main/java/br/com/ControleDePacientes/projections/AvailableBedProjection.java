package br.com.ControleDePacientes.projections;

import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.enums.SpecialtyEnum;

public interface AvailableBedProjection {
    String getSpecialty();
    Long getBedId();
    String getBedCode();
    String getBedStatus();
    Long getRoomId();
    String getRoomCode();
}

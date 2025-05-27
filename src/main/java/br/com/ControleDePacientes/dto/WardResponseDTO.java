package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import br.com.ControleDePacientes.model.WardModel;
import lombok.Data;

@Data
public class WardResponseDTO {
    private Long id;
    private SpecialtyEnum specialty;
    private Long hospitalId;
    private String hospitalName;

    public WardResponseDTO(WardModel ward) {
        this.id = ward.getId();
        this.specialty = ward.getSpecialty();

        //Essa verificação é uma boa por causa do LAZY que usei nas classes...
        if (ward.getHospital() != null) {
            this.hospitalId = ward.getHospital().getId();
            this.hospitalName = ward.getHospital().getName();
        }
    }
}

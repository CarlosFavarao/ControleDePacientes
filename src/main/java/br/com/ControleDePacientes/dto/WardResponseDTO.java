package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.model.WardModel;
import br.com.ControleDePacientes.enums.SpecialtyEnum;
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
        if (ward.getHospital() != null) { //Utilizado devido ao Fetch Type
            this.hospitalId = ward.getHospital().getId();
            this.hospitalName = ward.getHospital().getName();
        }
    }
}
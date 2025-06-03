package br.com.ControleDePacientes.dto.wards;

import br.com.ControleDePacientes.model.WardModel;
import br.com.ControleDePacientes.enums.SpecialtyEnum;
import lombok.Data;

@Data
public class WardResponseDTO { //Criei essa classe novamente, ela resolve muito bem o problema do Loop infinito no retorno
    private Long id;
    private SpecialtyEnum specialty;
    private Long hospitalId;
    private String hospitalName;

    public WardResponseDTO(WardModel ward) {
        this.id = ward.getId();
        this.specialty = ward.getSpecialty();
        if (ward.getHospital() != null) { //Utilizado por causa do Fetch Type
            this.hospitalId = ward.getHospital().getId();
            this.hospitalName = ward.getHospital().getName();
        }
    }
}
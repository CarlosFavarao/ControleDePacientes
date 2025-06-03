package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.SpecialtyEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpecialtyBedStatsDTO {
    private SpecialtyEnum specialty;
    private Long totalBeds;
    private Long freeBeds;
    private Long occupiedBeds;

    public SpecialtyBedStatsDTO(String specialty, Number totalbeds, Number freeBeds, Number occupiedBeds){
        this.specialty = SpecialtyEnum.valueOf(specialty);

        if (totalbeds != null){
            this.totalBeds = totalbeds.longValue(); //Se não for nulo, recebe o valor do Banco de Dados
        }else{
            this.totalBeds = 0L; //Se for, recebe O no tipo long, isso se repete nos outros ifs
        }

        if (freeBeds != null){
            this.freeBeds = freeBeds.longValue();
        }else{
            this.freeBeds = 0L;
        }

        if (occupiedBeds != null){
            this.occupiedBeds = occupiedBeds.longValue();
        }else{
            this.occupiedBeds = 0L;
        }

    }
}

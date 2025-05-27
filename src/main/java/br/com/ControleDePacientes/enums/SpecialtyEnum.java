package br.com.ControleDePacientes.enums;

import lombok.Getter;

@Getter
public enum SpecialtyEnum {
    CARDIOLOGY("CAR", "Cardiologia"),
    NEUROLOGY("NEU", "Neurologia"),
    ORTHOPEDICS("ORT", "Ortopedia"),
    PEDIATRICS("PED", "Pediatria"),
    GENERAL_CLINIC("CLI", "Clínica Geral"),
    ONCOLOGY("ONC", "Oncologia");

    private final String prefix;
    private final String description;

    SpecialtyEnum(String prefix, String description){
        this.prefix = prefix;
        this.description = description;
    }
}

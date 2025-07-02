package br.com.ControleDePacientes.enums;

public enum BedType {
    INFIRMARY("Enfermaria"),
    UTI("UTI");

    private final String name;

    private BedType(String name) {
        this.name = name;
    }
}

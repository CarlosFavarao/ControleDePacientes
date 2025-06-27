package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.ExamType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamDto {

    private String name;
    private LocalDateTime dateTime;
    private ExamType type;
    private Long paientId;
    private Long doctorId;
}

package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.enums.ExamStatus;
import br.com.ControleDePacientes.enums.ExamType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamDto {

    private String examName;
    private LocalDateTime dateTime;
    private ExamType type;
    private ExamStatus status;
    private Long patientId;
    private Long doctorId;
}

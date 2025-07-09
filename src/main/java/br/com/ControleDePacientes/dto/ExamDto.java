package br.com.ControleDePacientes.dto;

import br.com.ControleDePacientes.model.ExamModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExamDto {
    private String examName;
    private LocalDateTime dateTime;
    private String type;
    private String status;
    private String namePatient;
    private String doctorName;

    private Long patientId;
    private Long doctorId;


    public static ExamDto from(ExamModel examModel) {
        ExamDto dto = new ExamDto();
        dto.setExamName(examModel.getExamName());
        dto.setDateTime(examModel.getDateTime());
        dto.setType(examModel.getType().name());
        dto.setStatus(examModel.getStatus().name());
        dto.setNamePatient(examModel.getPatient().getName());
        dto.setDoctorName(examModel.getDoctor().getNameDoctor());
        return dto;
    }
}






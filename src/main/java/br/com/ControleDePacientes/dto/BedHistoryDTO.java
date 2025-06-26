package br.com.ControleDePacientes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BedHistoryDTO {
    private String patientName;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;

    public BedHistoryDTO(String patientName, Timestamp admissionDateSQL, Timestamp dischargeDateSQL) {
        this.patientName = patientName;
        this.admissionDate = (admissionDateSQL != null) ? admissionDateSQL.toLocalDateTime() : null;
        this.dischargeDate = (dischargeDateSQL != null) ? dischargeDateSQL.toLocalDateTime() : null;
    }
}
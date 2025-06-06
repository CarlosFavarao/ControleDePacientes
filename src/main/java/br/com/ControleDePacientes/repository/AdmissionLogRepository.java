package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.dto.BedHistoryDTO;
import br.com.ControleDePacientes.dto.PatientLocationDTO;
import br.com.ControleDePacientes.model.AdmissionLogModel;
import br.com.ControleDePacientes.projections.LogProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface AdmissionLogRepository extends JpaRepository<AdmissionLogModel, Long> {

    @Query("SELECT al FROM AdmissionLogModel al " +
            "JOIN FETCH al.patient p " +
            "JOIN FETCH al.bed b " +
            "WHERE p.id = :patientId AND al.dischargeDate IS NULL")
    Optional<AdmissionLogModel> findActiveAdmissionByPatientId(@Param("patientId") Long patientId);

    //Lista pacientes internados no momento, ordenados por ala e alfabeticamente.
    @Query(nativeQuery = true, value = //Query nativa, estudar mais sobre isso. É muito mais eficiente e reduz muitas linhas de código.
            "select " +
            "   p.name, " +
            "   w.specialty, " +
            "   al.admission_date as admissionDate, " +
            "   date (now()) - date (al.admission_date) as daysAdmitted " +
            "from " +
            "   admission_logs al " +
            "join patients p on " +
            "   p.id = al.patient_id " +
            "join beds b on " +
            "   b.id = al.bed_id " +
            "join rooms r on " +
            "   r.id = b.room_id " +
            "join wards w on " +
            "   w.id = r.ward_id " +
            "where " +
            "   al.discharge_date is null " +
            "order by " +
            "   w.specialty, " +
            "   name, " +
            "   admission_date;")
    List<LogProjection> findActiveAdmissions();

    //Faz o retorno de forma páginada do histórico de um usuário
    @Query(nativeQuery = true, value =
    "select " +
            "p.name as name, " +
            "w.specialty as specialty, " +
            "al.admission_date as admissionDate, " +
            "al.discharge_date as dischargeDate, " +
            "case " +
            "when al.discharge_date is not null then cast(DATE_PART('day', al.discharge_date - al.admission_date) as INTEGER) " +
            "else cast (DATE_PART('day', CURRENT_TIMESTAMP - al.admission_date) as INTEGER) " +
            "end as daysAdmitted " +
            "FROM " +
            "    admission_logs al " +
            "JOIN " +
            "    patients p ON p.id = al.patient_id " +
            "JOIN " +
            "    beds b ON b.id = al.bed_id " +
            "JOIN " +
            "    rooms r ON r.id = b.room_id " +
            "JOIN " +
            "    wards w ON w.id = r.ward_id " +
            "WHERE " +
            "    al.patient_id = :patientId " +
            "ORDER BY " +
            "    al.admission_date DESC;")
    Page<LogProjection> findAdmissionHistoryByPatientId(@Param("patientId") Long patientId, Pageable pageable);

    //Retorna o histórico de internação de um leito
    @Query(nativeQuery = true, value = "select " +
            "p.name as patientName, " +
            "al.admission_date as admissionDate, " +
            "al.discharge_date as dischargeDate " +
            "from " +
            "   admission_logs al " +
            "join " +
            "   patients p on al.patient_id = p.id " +
            "where " +
            "   al.bed_id = :bedId " +
            "order by " +
            "   al.admission_date desc;")
    Page<BedHistoryDTO>findBedAdmissionHistoryById(@Param ("bedId")Long BedId, Pageable pageable);
}

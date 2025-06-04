package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.dto.PatientLocationDTO;
import br.com.ControleDePacientes.model.AdmissionLogModel;
import br.com.ControleDePacientes.projections.LogProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface AdmissionLogRepository extends JpaRepository<AdmissionLogModel, Long> {

    //Mostrar quarto que paciente está internado. Passou por alguns refinamentos antes de ir para o commit (para função na service não ficar gigante!!)
    @Query("SELECT new br.com.ControleDePacientes.dto.PatientLocationDTO(" +
            "    p.id, p.name, h.name, w.specialty, r.code, b.code, al.admissionDate) " +
            "FROM AdmissionLogModel al " +
            "JOIN al.patient p " +
            "JOIN al.bed b " +
            "JOIN b.room r " +
            "JOIN r.ward w " +
            "JOIN w.hospital h " +
            "WHERE p.id = :patientId AND al.dischargeDate IS NULL")
    Optional<PatientLocationDTO> findPatientLocationDetails(@Param("patientId") Long patientId);

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
}

package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.dto.PatientLocationDTO;
import br.com.ControleDePacientes.model.AdmissionLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Optional;


public interface AdmissionLogRepository extends JpaRepository<AdmissionLogModel, Long> {

    //Mostrar quarto que paciente está internado. Passou por alguns refinos antes de ir pro commit (pra função na service não ficar gigante!!)
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
}

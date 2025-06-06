package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.dto.PatientLocationDTO;
import br.com.ControleDePacientes.model.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository <PatientModel, Long> {
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
}

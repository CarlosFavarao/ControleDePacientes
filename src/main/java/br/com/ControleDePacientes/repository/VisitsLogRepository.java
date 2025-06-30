package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.dto.BedHistoryDTO;
import br.com.ControleDePacientes.dto.PatientLocationDTO;
import br.com.ControleDePacientes.dto.VisitsLogResponseDTO;
import br.com.ControleDePacientes.model.AdmissionLogModel;
import br.com.ControleDePacientes.model.VisitsLogModel;
import br.com.ControleDePacientes.projections.LogProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VisitsLogRepository extends JpaRepository<VisitsLogModel, Long> {
    @Query("SELECT vl FROM VisitsLogModel vl " +
            "JOIN FETCH vl.patient p " +
            "JOIN FETCH vl.visitor v " +
            "WHERE p.id = :patientId AND vl.exitDate IS NULL")
    Optional<VisitsLogModel> findActiveVisitByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT vl FROM VisitsLogModel vl " +
            "JOIN FETCH vl.patient p " +
            "JOIN FETCH vl.visitor v " +
            "WHERE v.id = :visitorId AND vl.exitDate IS NULL")
    Optional<VisitsLogModel> findActiveVisitByVisitorId(@Param("visitorId") Long visitorId);

    VisitsLogModel findByVisitorId(Long visitorId);
}

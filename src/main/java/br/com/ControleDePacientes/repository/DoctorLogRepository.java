package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.model.DoctorLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoctorLogRepository extends JpaRepository<DoctorLogModel, Long> {

    @Query("SELECT d FROM DoctorLogModel d WHERE d.admission.id = :admissionId AND d.endTime IS NULL")
    DoctorLogModel findActiveByAdmission(@Param("admissionId") Long admissionId);
}


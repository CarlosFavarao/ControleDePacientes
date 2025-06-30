package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.model.DoctorHistoryModel;
import org.apache.el.stream.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoctorHistoryRepository extends JpaRepository<DoctorHistoryModel, Long> {

    @Query("SELECT H FROM DoctorHistoryModel H WHERE H.admission.id = :admissionId AND H.endTime IS NULL")
    DoctorHistoryModel findActiveByAdmission(@Param("admissionId") Long admissionId);
}


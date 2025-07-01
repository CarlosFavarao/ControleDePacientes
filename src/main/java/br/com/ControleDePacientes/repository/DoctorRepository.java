package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.model.DoctorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoctorRepository extends JpaRepository<DoctorModel, Long> {

    @Query("SELECT d FROM DoctorModel d " +
            "WHERE d.crm = :crm")
    DoctorModel findDoctorByCRM(@Param("crm") Long crm);
}

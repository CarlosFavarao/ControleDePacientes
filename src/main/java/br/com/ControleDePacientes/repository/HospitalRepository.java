package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.model.HospitalModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository <HospitalModel, Long>{
    List<HospitalModel> findByName(String name);
}

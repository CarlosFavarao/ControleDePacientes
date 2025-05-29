package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.model.AdmissionLogModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdmissionLogRepository extends JpaRepository<AdmissionLogModel, Long> {

}

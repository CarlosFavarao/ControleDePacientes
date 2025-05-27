package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.model.BedModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedRepository extends JpaRepository<BedModel, Long> {


}

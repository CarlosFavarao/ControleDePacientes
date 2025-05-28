package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.model.WardModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends JpaRepository<WardModel, Long> {
}

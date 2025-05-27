package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.model.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository <PatientModel, Long> {
    //Serão adicionadas coisas aqui...
}

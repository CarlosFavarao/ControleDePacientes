package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository <RoomModel, Long> {
    //Vão haver adições futuras...
}

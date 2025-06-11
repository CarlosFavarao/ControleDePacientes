package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.dto.AvailableRoomDTO;
import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository <RoomModel, Long> {
    //Melhoria na criação de quartos, para criar com prefixos corretos
    //long countByCodeStartingWith(String prefix);

    @Query(nativeQuery = true, value = "SELECT CAST(substring(r.code FROM '[0-9]+$') as INTEGER)" +
            "FROM rooms r " +
            "JOIN wards w ON r.ward_id = w.id " +
            "WHERE w.hospital_id = :hospitalId AND w.specialty = :specialtyName"
    )
    List<Integer> findExistingRoomNumbers(@Param("hospitalId") Long hospitalId, @Param("specialtyName") String specialtyName);

    @Query("SELECT new br.com.ControleDePacientes.dto.AvailableRoomDTO(r.ward.specialty, r.code) " +
            "FROM RoomModel r WHERE EXISTS (SELECT b FROM BedModel b WHERE b.room = r AND b.status = :status)")
    List<AvailableRoomDTO> findRoomsWithBedsByStatus(@Param("status") BedStatus status);
}

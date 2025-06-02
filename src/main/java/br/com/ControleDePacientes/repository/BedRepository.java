package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.model.BedModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<BedModel, Long> {

    @Query("SELECT b FROM BedModel b WHERE b.room.ward.hospital.id = :hospitalId")
    List<BedModel> findBedsByHospitalId(@Param("hospitalId") Long hospitalId);
}

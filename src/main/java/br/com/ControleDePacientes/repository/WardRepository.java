package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.dto.SpecialtyBedStatsDTO;
import br.com.ControleDePacientes.model.WardModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<WardModel, Long> {

    boolean existsByHospitalId(Long hospitalId);

    @Query(value = "SELECT " +
                    "w.specialty AS specialty, " +
                    "CAST(COUNT(b.id) AS BIGINT) AS totalBeds, " +                                             //Estudar depois!
                    "CAST(SUM(CASE WHEN b.status = 'AVAILABLE' THEN 1 ELSE 0 END) AS BIGINT) AS freeBeds,  " + //Aprender melhor sobre as queries, ainda não consigo fazer elas sozinho.
                    "CAST(SUM(CASE WHEN b.status = 'OCCUPIED' THEN 1 ELSE 0 END) AS BIGINT) AS occupiedBeds " +
                    "FROM wards w " +
                    "JOIN rooms r ON w.id = r.ward_id " +
                    "JOIN beds b ON r.id = b.room_id " +
                    "GROUP BY w.specialty",
                    nativeQuery = true)
    List<SpecialtyBedStatsDTO> getBedStatsBySpecialty();

    @Query("SELECT DISTINCT w FROM WardModel w " +
            "LEFT JOIN FETCH w.hospital " +
            "LEFT JOIN FETCH w.rooms r " +
            "LEFT JOIN FETCH r.beds b")
    List<WardModel> findAllWithRoomsAndBeds();
}

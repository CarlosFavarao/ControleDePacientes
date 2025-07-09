package br.com.ControleDePacientes.repository;

import br.com.ControleDePacientes.model.VisitorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<VisitorModel, Long> {
    Optional<VisitorModel> findById(Long visitorId);

    List<VisitorModel> findByNameContainingIgnoreCase(String name);

    @Query(nativeQuery = true,
            value = "SELECT * FROM visitors p order by p.id")
    List<VisitorModel> findAllOrderById();

    boolean existsByDocument(String document);

}

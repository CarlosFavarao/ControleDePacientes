package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.model.VisitorModel;
import br.com.ControleDePacientes.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VisitorService {
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private VisitsLogService visitsLogService;


    @Transactional
    public VisitorModel saveVisitor(VisitorModel visitor) {
        return this.visitorRepository.save(visitor);
    }

    @Transactional(readOnly = true)
    public List<VisitorModel> listVisitors() {
        return this.visitorRepository.findAllOrderById();
    }

    @Transactional
    public List<VisitorModel> findByName(String name) {
        return this.visitorRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public VisitorModel findById(Long id) {
        return visitorRepository.findById(id).orElseThrow(() -> new RuntimeException("Visitante não encontrado."));
    }

    @Transactional
    public void deleteVisitor(Long id) {
        if (visitsLogService.findByVisitorId(id) != null) {
            throw new IllegalArgumentException("Visitante não pode ser excluído pois já possui visitas registradas.");
        }
        this.visitorRepository.deleteById(id);
    }
}
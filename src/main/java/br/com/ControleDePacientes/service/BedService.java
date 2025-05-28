package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.model.BedModel;
import br.com.ControleDePacientes.repository.BedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BedService {
    @Autowired
    private BedRepository bedRepository;

    public List<BedModel> findAllBeds(){
        return  bedRepository.findAll();
    }

    public Optional<BedModel> findByBedId(Long id){
        return bedRepository.findById(id);
    }
}

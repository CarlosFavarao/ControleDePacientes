package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.BedResponseDTO;
import br.com.ControleDePacientes.model.BedModel;
import br.com.ControleDePacientes.repository.BedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BedService {
    @Autowired
    private BedRepository bedRepository;

    public List<BedResponseDTO> findAllBeds(){
        return  bedRepository.findAll().stream().map(BedResponseDTO::new).collect(Collectors.toList());
    }

    public Optional<BedResponseDTO> findByBedId(Long id){
        return bedRepository.findById(id).map(BedResponseDTO::new);
    }

    public List<BedResponseDTO> findBedsByHospitalId(Long hospitalId){
        List<BedModel> beds = bedRepository.findBedsByHospitalId(hospitalId);

        return beds.stream().map(BedResponseDTO::new).collect(Collectors.toList());
    }
}

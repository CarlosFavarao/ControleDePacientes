package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.AvailableBedDTO;
import br.com.ControleDePacientes.dto.BedResponseDTO;
import br.com.ControleDePacientes.model.BedModel;
import br.com.ControleDePacientes.projections.AvailableBedProjection;
import br.com.ControleDePacientes.repository.BedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BedService {
    @Autowired
    private BedRepository bedRepository;

    public List<BedResponseDTO> findAllBeds(){
        return this.bedRepository.findAll().stream().map(BedResponseDTO::new).collect(Collectors.toList());
    }

    public Optional<BedResponseDTO> findByBedId(Long id){
        return this.bedRepository.findById(id).map(BedResponseDTO::new);
    }

    public List<BedResponseDTO> findBedsByHospitalId(Long hospitalId){
        List<BedModel> beds = bedRepository.findBedsByHospitalId(hospitalId);

        return beds.stream().map(BedResponseDTO::new).collect(Collectors.toList());
    }

    public Page<AvailableBedDTO> findAvailableBeds(Pageable pageable){
        Page<AvailableBedProjection> projectionPage = bedRepository.findAvailableBeds(pageable);

        return projectionPage.map(AvailableBedDTO::fromProjection);
    }
}

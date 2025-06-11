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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BedService {
    @Autowired
    private BedRepository bedRepository;

    @Transactional(readOnly = true)
    public List<BedResponseDTO> findAllBeds(){
        return this.bedRepository.findAll().stream().map(BedResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<BedResponseDTO> findByBedId(Long id){
        return this.bedRepository.findById(id).map(BedResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public List<BedResponseDTO> findBedsByHospitalId(Long hospitalId){
        List<BedModel> beds = bedRepository.findBedsByHospitalId(hospitalId);

        return beds.stream().map(BedResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<AvailableBedDTO> findAvailableBeds(Pageable pageable){
        Page<AvailableBedProjection> projectionPage = bedRepository.findAvailableBeds(pageable);

        return projectionPage.map(AvailableBedDTO::fromProjection);
    }

    @Transactional(readOnly = true)
    public Page<AvailableBedDTO> findAvailableBedsByHospitalId(Long hospitalId, String specialtyName, Pageable pageable){
        Page<AvailableBedProjection> projectionPage = bedRepository.findAvailableBedsByHospitalId(hospitalId, specialtyName, pageable);

        return projectionPage.map(AvailableBedDTO::fromProjection);
    }
}

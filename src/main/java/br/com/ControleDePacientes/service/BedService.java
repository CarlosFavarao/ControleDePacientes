package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.AvailableBedDTO;
import br.com.ControleDePacientes.dto.BedResponseDTO;
import br.com.ControleDePacientes.model.BedModel;
import br.com.ControleDePacientes.projections.AvailableBedProjection;
import br.com.ControleDePacientes.repository.BedRepository;
import jakarta.persistence.EntityNotFoundException;
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

    @Transactional
    public BedModel save(BedModel bed){
        return this.bedRepository.save(bed);
    }

    @Transactional(readOnly = true)
    public List<BedResponseDTO> findAllBeds(){
        return this.bedRepository.findAll().stream().map(BedResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BedModel findById(Long id){
        return this.bedRepository.findById(id).orElseThrow(() -> new RuntimeException("Cama não encontrada."));
    }

    @Transactional(readOnly = true)
    public Optional<BedResponseDTO> findByBedId(Long id){
        return this.bedRepository.findById(id).map(BedResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<BedResponseDTO> findBedsByHospitalId(Long hospitalId, Pageable pageable){
        Page<BedModel> beds = this.bedRepository.findBedsByHospitalId(hospitalId, pageable);

        return beds.map(BedResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<AvailableBedDTO> findAvailableBeds(Pageable pageable){
        Page<AvailableBedProjection> projectionPage = this.bedRepository.findAvailableBeds(pageable);

        return projectionPage.map(AvailableBedDTO::fromProjection);
    }

    @Transactional(readOnly = true)
    public Page<AvailableBedDTO> findAvailableBedsByHospitalId(Long hospitalId, Pageable pageable){
        Page<AvailableBedProjection> projectionPage = this.bedRepository.findAvailableBedsByHospitalId(hospitalId, pageable);

        return projectionPage.map(AvailableBedDTO::fromProjection);
    }

    @Transactional(readOnly = true)
    public Page<AvailableBedDTO> findAvailableBedsByHospitalIdAndSpecialty(Long hospitalId, String specialtyName, Pageable pageable){
        Page<AvailableBedProjection> projectionPage = this.bedRepository.findAvailableBedsByHospitalIdAndSpecialty(hospitalId, specialtyName, pageable);

        return projectionPage.map(AvailableBedDTO::fromProjection);
    }

    @Transactional(readOnly = true)
    public Long findWardByBedId(Long bedId){
        return this.bedRepository.findWardIdByBedId(bedId)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma Ala encontrada."));
    }
}

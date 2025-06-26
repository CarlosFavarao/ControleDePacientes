package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.AvailableBedDTO;
import br.com.ControleDePacientes.dto.BedResponseDTO;
import br.com.ControleDePacientes.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/beds")
public class BedController {
    @Autowired
    private BedService bedService;

    @GetMapping
    public List<BedResponseDTO> findAllBeds(){
        return this.bedService.findAllBeds();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedResponseDTO> findBedById(@PathVariable Long id){
        return this.bedService.findByBedId(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/hospital/{hospitalId}") //exibir camas do hospital
    public ResponseEntity<Page<BedResponseDTO>> getBedsByHospitalId(@PathVariable Long hospitalId, Pageable pageable){
        Page<BedResponseDTO> beds = this.bedService.findBedsByHospitalId(hospitalId, pageable);
        return ResponseEntity.ok(beds);
    }

    @GetMapping("/available") //Leitos livres, no geral e com detalhes.
    public ResponseEntity<Page<AvailableBedDTO>> getAvailableBeds(Pageable pageable) {
        Page<AvailableBedDTO> availableBedsPage = this.bedService.findAvailableBeds(pageable);
        return ResponseEntity.ok(availableBedsPage);
    }

    @GetMapping("/available-by-hospital/{hospitalId}")
    public ResponseEntity<Page<AvailableBedDTO>> getAvailableBedsByHospitalId(@PathVariable Long hospitalId, Pageable pageable){
        Page<AvailableBedDTO> bedsPage = this.bedService.findAvailableBedsByHospitalId(hospitalId, pageable);
        return ResponseEntity.ok(bedsPage);
    }

    @GetMapping("/available-by-hospital-and-specialty/{hospitalId}/{specialtyName}")
    public ResponseEntity<Page<AvailableBedDTO>> getAvailableBedsByHospitalIdAndSpecialty(@PathVariable Long hospitalId, @PathVariable String specialtyName, Pageable pageable){
        Page<AvailableBedDTO> bedsPage = this.bedService.findAvailableBedsByHospitalIdAndSpecialty(hospitalId, specialtyName, pageable);
        return ResponseEntity.ok(bedsPage);
    }
}

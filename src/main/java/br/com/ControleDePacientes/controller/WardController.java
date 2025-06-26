package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.SpecialtyBedStatsDTO;
import br.com.ControleDePacientes.dto.SpecialtyRoomStatsDTO;
import br.com.ControleDePacientes.dto.WardCreateRequestDTO;
import br.com.ControleDePacientes.dto.WardResponseDTO;
import br.com.ControleDePacientes.model.WardModel;
import br.com.ControleDePacientes.service.WardService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wards")
public class WardController {
    @Autowired
    private WardService wardService;

    @PostMapping
    public ResponseEntity<WardModel> createWard(@RequestBody WardCreateRequestDTO dto) {
        return ResponseEntity.ok(this.wardService.createWardWithRoomsAndBeds(dto));
    }

    @GetMapping
    public ResponseEntity<List<WardResponseDTO>> getAllWards() {
        List<WardResponseDTO> wards = this.wardService.findAllWards();
        return ResponseEntity.ok(wards);
    }

    @GetMapping("/{id}")
    public ResponseEntity findWardById(@PathVariable Long id) {
        return ResponseEntity.ok(this.wardService.findWardById(id));
    }

    @GetMapping("/stats/beds-by-specialty") //Quantidade de leitos livres por cada especialidade
    public ResponseEntity<List<SpecialtyBedStatsDTO>> getBedStatsBySpecialty(){
        List<SpecialtyBedStatsDTO> stats = this.wardService.getBedStatsBySpecialty();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/room-by-specialty") //Quartos não ocupados por cada especialidade
    public ResponseEntity<List<SpecialtyRoomStatsDTO>> getRoomStatsBySpecialty() {
        List<SpecialtyRoomStatsDTO> stats = this.wardService.getRoomStatsBySpecialty();
        return ResponseEntity.ok(stats);
    }
}
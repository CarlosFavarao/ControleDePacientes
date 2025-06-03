package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.specialty.SpecialtyBedStatsDTO;
import br.com.ControleDePacientes.dto.specialty.SpecialtyRoomStatsDTO;
import br.com.ControleDePacientes.dto.wards.WardCreateRequestDTO;
import br.com.ControleDePacientes.dto.wards.WardResponseDTO;
import br.com.ControleDePacientes.model.WardModel;
import br.com.ControleDePacientes.service.WardService;
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
    public ResponseEntity<WardResponseDTO> findWardById(@PathVariable Long id) {
        return this.wardService.findWardById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/stats/beds-by-specialty") //Quantidade de leitos livres por cada especialidade (posso adaptar para ficar melhor)
    public ResponseEntity<List<SpecialtyBedStatsDTO>> getBedStatsBySpecialty(){
        List<SpecialtyBedStatsDTO> stats = wardService.getBedStatsBySpecialty();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/room-stats-by-specialty")
    public ResponseEntity<List<SpecialtyRoomStatsDTO>> getRoomStatsBySpecialty() {
        List<SpecialtyRoomStatsDTO> stats = wardService.getRoomStatsBySpecialty();
        return ResponseEntity.ok(stats);
    }
}
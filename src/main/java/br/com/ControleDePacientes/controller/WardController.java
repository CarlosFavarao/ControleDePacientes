package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.WardCreateRequestDTO;
import br.com.ControleDePacientes.dto.WardResponseDTO;
import br.com.ControleDePacientes.model.WardModel;
import br.com.ControleDePacientes.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/wards")
public class WardController {
    @Autowired
    private WardService wardService;

    @PostMapping
    public ResponseEntity<WardResponseDTO> createWard(@RequestBody WardCreateRequestDTO dto) {
        WardModel createWard = wardService.createWardWithRoomsAndBeds(dto);

        WardResponseDTO responseDTO = new WardResponseDTO(createWard);

        return ResponseEntity.status(201).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<WardResponseDTO>> getAllWards() {
        List<WardResponseDTO> wards = wardService.findAllWards();
        return ResponseEntity.ok(wards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WardResponseDTO> getWardById(@PathVariable Long id) {
        Optional<WardResponseDTO> wardDTOOptional = wardService.findWardById(id);

        return wardDTOOptional
                .map(ResponseEntity::ok) // Forma curta de .map(dto -> ResponseEntity.ok(dto))
                .orElseGet(ResponseEntity.notFound()::build); // Forma curta de .orElseGet(() -> ResponseEntity.notFound().build())
    }

}

package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.WardCreateRequestDTO;
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
    public ResponseEntity<List<WardModel>> getAllWards() {
        List<WardModel> wards = this.wardService.findAllWards();
        return ResponseEntity.ok(wards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WardModel> findWardById(@PathVariable Long id) {
        return ResponseEntity.ok(this.wardService.findWardById(id));
    }
}
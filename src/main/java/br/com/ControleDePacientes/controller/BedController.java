package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.BedResponseDTO;
import br.com.ControleDePacientes.model.BedModel;
import br.com.ControleDePacientes.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/beds")
public class BedController {
    @Autowired
    private BedService bedService;

    @GetMapping
    public List<BedResponseDTO> findAllBeds(){
        return bedService.findAllBeds();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedResponseDTO> findBedById(@PathVariable Long id){
        return bedService.findByBedId(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

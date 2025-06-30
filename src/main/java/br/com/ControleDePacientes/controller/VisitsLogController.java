package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.*;
import br.com.ControleDePacientes.model.AdmissionLogModel;
import br.com.ControleDePacientes.service.AdmissionLogService;
import br.com.ControleDePacientes.service.VisitsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
public class VisitsLogController {
    @Autowired
    private VisitsLogService visitsLogService;

    @PostMapping //Registrar visita
    public ResponseEntity<VisitsLogResponseDTO> registerVisit(@RequestBody VisitsLogRequestDTO visitRequest) {
        return ResponseEntity.ok(this.visitsLogService.registerVisit(visitRequest));
    }

//    @GetMapping("/currently-visited") //Exibir os pacientes visitados atualmente
//    public ResponseEntity<List<VisitsLogResponseDTO>> getCurrentlyVisitedPatients(){
//        return ResponseEntity.ok(this.visitsLogService.getCurrentlyVisitedPatients());
//    }

    @PutMapping("/unregister/{visitorId}") //Liberar visita
    public ResponseEntity<VisitsLogResponseDTO> unregisterVisit(@PathVariable Long visitorId){
        return ResponseEntity.ok(this.visitsLogService.unregisterVisit(visitorId));
    }

}

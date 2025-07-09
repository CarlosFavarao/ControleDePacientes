package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.*;
import br.com.ControleDePacientes.service.VisitsLogService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/history/patient/{patientId}")
    public ResponseEntity<List<VisitsLogResponseDTO>> getVisitByPatientId(@PathVariable Long patientId){
        return ResponseEntity.ok(this.visitsLogService.getVisitByPatientId(patientId));
    }

    @GetMapping("/history/visitor/{visitorId}")
    public ResponseEntity<List<VisitsLogResponseDTO>> getVisitByVisitorId(@PathVariable Long visitorId){
        return ResponseEntity.ok(this.visitsLogService.getVisitByVisitorId(visitorId));
    }


    @GetMapping("/currently-visited")
    public ResponseEntity<List<VisitsLogResponseDTO>> getCurrentlyVisitedPatients(){
        return ResponseEntity.ok(this.visitsLogService.getCurrentlyVisitedPatients());
    }

    @GetMapping("/closed-visits")
    public ResponseEntity<List<VisitsLogResponseDTO>> getClosedVisits(){
        return ResponseEntity.ok(this.visitsLogService.getClosedVisits());
    }

    @PutMapping("/release/{visitorId}")
    public ResponseEntity<VisitsLogResponseDTO> unregisterVisit(@PathVariable Long visitorId){
        return ResponseEntity.ok(this.visitsLogService.unregisterVisit(visitorId));
    }

}

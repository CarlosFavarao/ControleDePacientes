package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.*;
import br.com.ControleDePacientes.model.AdmissionLogModel;
import br.com.ControleDePacientes.service.AdmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adm")
public class AdmissionController {
    @Autowired
    private AdmissionService admissionService;

    @PostMapping //Internar
    public AdmissionResponseDTO admitPatient(@RequestBody AdmissionRequestDTO admissionRequest) {
        return admissionService.admitPatient(admissionRequest);
    }

    @GetMapping("/patient-location/{patientId}") //Encontrar
    public ResponseEntity<PatientLocationDTO> getPatientLocation(@PathVariable Long patientId) { //Se não fizer sentido, posso mover para patient
        return admissionService.findPatientLocation(patientId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/currently-admitted") //Exibir os pacientes admitidos, filtrando por Ala e nome. ALém de exibir dias internados de cada paciente. Tudo isso usando query nativa!!!
    public ResponseEntity<List<LogDTO>> getCurrentlyAdmittedPatients(){
        return ResponseEntity.ok(admissionService.getCurrentlyAdmittedPatients());
    }

    @GetMapping("/history/patient/{patientId}") //Exibir histórico do paciente
    public Page<LogDTO> getAdmissionHistoryByPatientId(@PathVariable Long patientId, Pageable pageable){
        return admissionService.getAdmissionHistoryByPatientId(patientId, pageable);
    }

    @GetMapping("/history/bed/{bedId}")
    public Page<BedHistoryDTO> getBedAdmissionHistory(@PathVariable Long bedId, Pageable pageable) {
        return admissionService.getBedAdmissionHistory(bedId, pageable);
    }

    @PutMapping("/discharge/{patientId}") //Dar alta
    public AdmissionLogModel dischargePatient(@PathVariable Long patientId){
        return admissionService.dischargePatient(patientId);
    }
}

package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.AdmissionRequestDTO;
import br.com.ControleDePacientes.dto.AdmissionResponseDTO;
import br.com.ControleDePacientes.dto.AdmittedPatientDTO;
import br.com.ControleDePacientes.dto.PatientLocationDTO;
import br.com.ControleDePacientes.enums.SpecialtyEnum;
import br.com.ControleDePacientes.model.AdmissionLogModel;
import br.com.ControleDePacientes.service.AdmissionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<AdmittedPatientDTO>> getCurrentlyAdmittedPatients(){
        return ResponseEntity.ok(admissionService.getCurrentlyAdmittedPatients());
    }

    @PutMapping("/discharge/{patientId}") //Dar alta
    public AdmissionLogModel dischargePatient(@PathVariable Long patientId){
        return admissionService.dischargePatient(patientId);
    }
}

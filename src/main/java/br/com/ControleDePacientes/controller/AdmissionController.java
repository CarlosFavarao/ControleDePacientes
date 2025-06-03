package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.admission.AdmissionRequestDTO;
import br.com.ControleDePacientes.dto.admission.AdmissionResponseDTO;
import br.com.ControleDePacientes.dto.patient.PatientLocationDTO;
import br.com.ControleDePacientes.model.AdmissionLogModel;
import br.com.ControleDePacientes.service.AdmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PatientLocationDTO> getPatientLocation(@PathVariable Long patientId) { //Se não estiver fazendo sentido, posso mover para patient
        return admissionService.findPatientLocation(patientId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/discharge/{patientId}") //Dar alta
    public AdmissionLogModel dischargePatient(@PathVariable Long patientId){
        return admissionService.dischargePatient(patientId);
    }
}

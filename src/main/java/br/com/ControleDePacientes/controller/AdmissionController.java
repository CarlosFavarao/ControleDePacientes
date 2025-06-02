package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.AdmissionRequestDTO;
import br.com.ControleDePacientes.dto.PatientLocationDTO;
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

    @PostMapping
    public AdmissionLogModel admitPatient(@RequestBody AdmissionRequestDTO admissionRequest) {
        return admissionService.admitPatient(admissionRequest);
    }

    @GetMapping("/patient-location/{patientId}")
    public ResponseEntity<PatientLocationDTO> getPatientLocation(@PathVariable Long patientId) { //Se não estiver fazendo sentido, posso mover para patient
        return admissionService.findPatientLocation(patientId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.PatientLocationDTO;
import br.com.ControleDePacientes.model.PatientModel;
import br.com.ControleDePacientes.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientModel> savePatient(@RequestBody PatientModel patient){
        return ResponseEntity.ok(this.patientService.savePatient(patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientModel> updatePatient(@PathVariable Long id, @RequestBody PatientModel patient){
        return ResponseEntity.ok(this.patientService.updatePatient(id, patient));
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<PatientModel>> findPatientByName(@PathVariable String name){
        return ResponseEntity.ok(this.patientService.findByName(name));
    }

    @GetMapping
    public ResponseEntity<List<PatientModel>> listAllPatients(){
        return ResponseEntity.ok(this.patientService.listPatients());
    }

    @GetMapping("/location/{patientId}") //Encontrar
    public ResponseEntity<PatientLocationDTO> getPatientLocation(@PathVariable Long patientId) { //Se não fizer sentido, posso mover para patient
        return this.patientService.findPatientLocation(patientId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePatient(Long id){
        this.patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}

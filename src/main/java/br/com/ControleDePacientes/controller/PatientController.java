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
    public PatientModel savePatient(@RequestBody PatientModel patient){
        return this.patientService.savePatient(patient);
    }

    @PutMapping("/{id}")
    public PatientModel updatePatient(@PathVariable Long id, @RequestBody PatientModel patient){
        return this.patientService.updatePatient(id, patient);
    }

    @GetMapping("/search/{name}")
    public List<PatientModel> findPatientByName(@PathVariable String name){
        return this.patientService.findByName(name);
    }

    @GetMapping
    public List<PatientModel> listAllPatients(){
        return this.patientService.listPatients();
    }

    @GetMapping("/location/{patientId}") //Encontrar
    public ResponseEntity<PatientLocationDTO> getPatientLocation(@PathVariable Long patientId) { //Se não fizer sentido, posso mover para patient
        return this.patientService.findPatientLocation(patientId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deletePatient(Long id){
        this.patientService.deletePatient(id);
    }
}

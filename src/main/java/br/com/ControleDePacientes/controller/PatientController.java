package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.model.PatientModel;
import br.com.ControleDePacientes.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @PostMapping
    public PatientModel savePatient(@RequestBody PatientModel patient){
        return patientService.savePatient(patient);
    }

    @GetMapping
    public List<PatientModel> listAllPatients(){
        return patientService.listPatients();
    }

    @DeleteMapping("/{id}")
    public void deletePatient(Long id){
        patientService.deletePatient(id);
    }
}

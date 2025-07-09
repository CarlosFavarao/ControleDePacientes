package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.model.DoctorModel;
import br.com.ControleDePacientes.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorModel> saveDoctor(@RequestBody DoctorModel doctor){
        return ResponseEntity.ok(this.doctorService.saveDoctor(doctor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorModel> updateDoctor(@PathVariable Long id, @RequestBody DoctorModel doctor){
        return ResponseEntity.ok(this.doctorService.updateDoctor(id, doctor));
    }

    @GetMapping
    public ResponseEntity<List<DoctorModel>> listAlldoctors(){
        return ResponseEntity.ok(this.doctorService.listDoctors());
    }

    @GetMapping("/crm/{crm}")
    public ResponseEntity<DoctorModel> findDoctorByCRM(@PathVariable Long crm){
        return ResponseEntity.ok(this.doctorService.findDoctorByCRM(crm));
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id){
        this.doctorService.deleteDoctor(id);
    }

}

package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.model.DoctorModel;
import br.com.ControleDePacientes.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @PostMapping
    public DoctorModel saveDoctor(@RequestBody DoctorModel doctor){
        return this.doctorService.saveDoctor(doctor);
    }

    @PutMapping("/{id}")
    public DoctorModel updateDoctor(@PathVariable Long id, @RequestBody DoctorModel doctor){
        return this.doctorService.updateDoctor(id, doctor);
    }

    @GetMapping
    public List<DoctorModel> listAlldoctors(){
        return this.doctorService.listDoctors();
    }

    @GetMapping("/crm/{crm}")
    public DoctorModel findDoctorByCRM(Long crm){
        return this.doctorService.findDoctorByCRM(crm);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(Long id){
        this.doctorService.deleteDoctor(id);
    }

}

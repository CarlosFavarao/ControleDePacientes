package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.service.HospitalService;
import br.com.ControleDePacientes.model.HospitalModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hospitals")
public class HospitalController {
    @Autowired
    HospitalService hospitalService;

    @PostMapping
    public HospitalModel saveHospital(@RequestBody HospitalModel hospital){
        return this.hospitalService.saveHospital(hospital);
    }

    @PutMapping("/update/{id}")
    public HospitalModel updateHospital(@PathVariable Long id, @RequestBody HospitalModel hospital){
        return this.hospitalService.updateHospital(id, hospital);
    }

    @GetMapping
    public List<HospitalModel> listHospitals(){
        return this.hospitalService.listHospitals();
    }

    @GetMapping("/id/{id}")
    public Optional<HospitalModel> findHospitalById(@PathVariable Long id){
        return this.hospitalService.findHospitalById(id);
    }

    @GetMapping("/name/{name}")
    public List<HospitalModel> findHospitalByName(@PathVariable String name){
        return  this.hospitalService.findHospitalByName(name);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteHospital(@PathVariable Long id){
        this.hospitalService.deleteHospital(id);
    }

}

package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.BedResponseDTO;
import br.com.ControleDePacientes.service.BedService;
import br.com.ControleDePacientes.service.HospitalService;
import br.com.ControleDePacientes.model.HospitalModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hospitals")
public class HospitalController {
    @Autowired HospitalService hospitalService;

    @PostMapping
    public ResponseEntity<HospitalModel> saveHospital(@RequestBody HospitalModel hospital){
        return ResponseEntity.ok(this.hospitalService.saveHospital(hospital));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalModel> updateHospital(@PathVariable Long id, @RequestBody HospitalModel hospital){
        return ResponseEntity.ok(this.hospitalService.updateHospital(id, hospital));
    }

    @GetMapping
    public ResponseEntity<List<HospitalModel>> listHospitals(){
        return ResponseEntity.ok(this.hospitalService.listHospitals());
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<HospitalModel>> findHospitalByName(@PathVariable String name){
        return ResponseEntity.ok(this.hospitalService.findHospitalByName(name));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<HospitalModel> findHospitalById(@PathVariable Long id){
        return ResponseEntity.ok(this.hospitalService.findHospitalById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteHospital(@PathVariable Long id){
        this.hospitalService.deleteHospital(id);
        return ResponseEntity.noContent().build();
    }

}

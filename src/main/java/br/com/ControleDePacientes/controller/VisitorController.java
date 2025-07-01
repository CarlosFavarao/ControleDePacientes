package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.PatientLocationDTO;
import br.com.ControleDePacientes.model.PatientModel;
import br.com.ControleDePacientes.model.VisitorModel;
import br.com.ControleDePacientes.service.PatientService;
import br.com.ControleDePacientes.service.VisitorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visitors")
public class VisitorController {
    @Autowired
    private VisitorService visitorService;

    @PostMapping
    public VisitorModel saveVisitor(@RequestBody @Valid VisitorModel visitor) {
        return this.visitorService.saveVisitor(visitor);
    }

    @GetMapping("/search/{name}")
    public List<VisitorModel> findVisitorByName(@PathVariable String name){
        return this.visitorService.findByName(name);
    }

    @GetMapping
    public List<VisitorModel> listAllVisitors(){
        return this.visitorService.listVisitors();
    }

    @DeleteMapping("/{id}")
    public void deleteVisitor(@PathVariable Long id){
        this.visitorService.deleteVisitor(id);
    }
}
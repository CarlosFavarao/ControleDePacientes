package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.model.VisitorModel;
import br.com.ControleDePacientes.service.VisitorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visitors")
public class VisitorController {
    @Autowired
    private VisitorService visitorService;

    @PostMapping
    public ResponseEntity<VisitorModel> saveVisitor(@RequestBody @Valid VisitorModel visitor) {
        return ResponseEntity.ok(this.visitorService.saveVisitor(visitor));
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<VisitorModel>> findVisitorByName(@PathVariable String name){
        return ResponseEntity.ok(this.visitorService.findByName(name));
    }

    @GetMapping
    public ResponseEntity<List<VisitorModel>> listAllVisitors(){
        return ResponseEntity.ok(this.visitorService.listVisitors());
    }

    @DeleteMapping("/{id}")
    public void deleteVisitor(@PathVariable Long id){
        this.visitorService.deleteVisitor(id);
    }
}
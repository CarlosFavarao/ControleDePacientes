package br.com.ControleDePacientes.Controller;

import br.com.ControleDePacientes.Service.HospitalService;
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
        return hospitalService.saveHospital(hospital);
    }

    @GetMapping
    public List<HospitalModel> listHospitals(){
        return hospitalService.listHospitals();
    }

    @GetMapping("/id/{id}")
    public Optional<HospitalModel> findHospitalById(@PathVariable Long id){
        return hospitalService.findHospitalById(id);
    }

    @GetMapping("/{name}")
    public List<HospitalModel> findHospitalByName(@PathVariable String name){
        return  hospitalService.findHospitalByName(name);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteHospital(@PathVariable Long id){ //posso, futuramente, deletar por nome também... (talvez com o RequestParam diferente e um retorno de um FindByName)
        hospitalService.deleteHospital(id);
    }

}

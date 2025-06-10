package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.model.HospitalModel;
import br.com.ControleDePacientes.repository.HospitalRepository;
import br.com.ControleDePacientes.repository.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {
    @Autowired HospitalRepository hospitalRepository;

    @Autowired WardService wardService;

    public HospitalModel saveHospital(HospitalModel hospital){
        return this.hospitalRepository.save(hospital);
    }

    public HospitalModel updateHospital(Long id, HospitalModel updatedHospital){
        HospitalModel hospital = this.findHospitalById(id);
        hospital.setName(updatedHospital.getName());
        return this.hospitalRepository.save(hospital);
    }

    public List<HospitalModel> listHospitals(){
        return this.hospitalRepository.findAll();
    }

    public HospitalModel findHospitalById(Long id){
        return this.hospitalRepository.findById(id).orElseThrow(() -> new RuntimeException("Hospital não encontrado."));
    }

    public List<HospitalModel> findHospitalByName(String name){
        return this.hospitalRepository.findByName(name);
    }

    public void deleteHospital(Long id){
        if (!wardService.existsByHospitalId(id)) {
            this.hospitalRepository.deleteById(id);
        }
    }
}

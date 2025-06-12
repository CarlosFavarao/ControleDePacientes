package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.model.HospitalModel;
import br.com.ControleDePacientes.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HospitalService {
    @Autowired HospitalRepository hospitalRepository;

    @Lazy
    @Autowired WardService wardService;

    @Transactional
    public HospitalModel saveHospital(HospitalModel hospital){
        return this.hospitalRepository.save(hospital);
    }

    @Transactional
    public HospitalModel updateHospital(Long id, HospitalModel updatedHospital){
        HospitalModel hospital = this.findHospitalById(id);
        hospital.setName(updatedHospital.getName());
        return this.hospitalRepository.save(hospital);
    }

    @Transactional(readOnly = true)
    public List<HospitalModel> listHospitals(){
        return this.hospitalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public HospitalModel findHospitalById(Long id){
        return this.hospitalRepository.findById(id).orElseThrow(() -> new RuntimeException("Hospital não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<HospitalModel> findHospitalByName(String name){
        return this.hospitalRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public void deleteHospital(Long id){
        if (!wardService.existsByHospitalId(id)) {
            this.hospitalRepository.deleteById(id);
        }
    }
}

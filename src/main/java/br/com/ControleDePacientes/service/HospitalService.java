package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.model.HospitalModel;
import br.com.ControleDePacientes.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {
    @Autowired
    HospitalRepository hospitalRepository;

    public HospitalModel saveHospital(HospitalModel hospital){
        return hospitalRepository.save(hospital);
    }

    public HospitalModel updateHospital(Long id, HospitalModel updatedHospital){
        Optional<HospitalModel> currentHospital = hospitalRepository.findById(id);

        if(currentHospital.isPresent()){
            HospitalModel hospital = currentHospital.get();
            hospital.setName(updatedHospital.getName());
            return hospitalRepository.save(hospital);
        }
        return null;
    }

    public List<HospitalModel> listHospitals(){
        return hospitalRepository.findAll();
    }

    public Optional<HospitalModel> findHospitalById(Long id){
        return hospitalRepository.findById(id);
    }

    public List<HospitalModel> findHospitalByName(String name){
        return hospitalRepository.findByName(name);
    }

    public void deleteHospital(Long id){
        hospitalRepository.deleteById(id); //Vou alterar futuramente para retornar True ou False...
    }
}

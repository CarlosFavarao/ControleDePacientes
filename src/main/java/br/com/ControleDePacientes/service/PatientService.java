package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.model.PatientModel;
import br.com.ControleDePacientes.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    public PatientModel savePatient(PatientModel patient){
        return patientRepository.save(patient);
    }

    public List<PatientModel> listPatients(){
        return patientRepository.findAll();
    }

    public void deletePatient(Long id){
        patientRepository.deleteById(id);
    }
}

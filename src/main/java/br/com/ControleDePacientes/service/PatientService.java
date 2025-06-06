package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.PatientLocationDTO;
import br.com.ControleDePacientes.model.PatientModel;
import br.com.ControleDePacientes.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    public PatientModel savePatient(PatientModel patient){
        return patientRepository.save(patient);
    }

    public Optional<PatientLocationDTO> findPatientLocation(Long patientId) { //Encontrar paciente
        return patientRepository.findPatientLocationDetails(patientId);
    }

    public List<PatientModel> listPatients(){
        return patientRepository.findAll();
    }

    public void deletePatient(Long id){
        patientRepository.deleteById(id);
    }
}

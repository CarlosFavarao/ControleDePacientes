package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.PatientLocationDTO;
import br.com.ControleDePacientes.model.PatientModel;
import br.com.ControleDePacientes.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Transactional
    public PatientModel savePatient(PatientModel patient){
        return this.patientRepository.save(patient);
    }

    @Transactional(readOnly = true)
    public List<PatientModel> listPatients(){
        return this.patientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PatientLocationDTO> findPatientLocation(Long patientId) { //Encontrar paciente
        return this.patientRepository.findPatientLocationDetails(patientId);
    }

    @Transactional
    public List<PatientModel> findByName(String name){
        return this.patientRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public PatientModel findById(Long id){
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado."));
    }

    @Transactional
    public PatientModel updatePatient(Long id, PatientModel updatedPatient){
        PatientModel patient = this.findById(id);
        patient.setName(updatedPatient.getName());

        return this.patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Long id){
        this.patientRepository.deleteById(id);
    }
}

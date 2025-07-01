package br.com.ControleDePacientes.service;


import br.com.ControleDePacientes.model.DoctorModel;
import br.com.ControleDePacientes.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Transactional
    public DoctorModel saveDoctor(DoctorModel doctor){
        return this.doctorRepository.save(doctor);
    }

    @Transactional(readOnly = true)
    public List<DoctorModel> listDoctors(){
        return this.doctorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DoctorModel findById(Long id){
        return doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado."));
    }

    @Transactional
    public DoctorModel updateDoctor(Long id, DoctorModel updateddoctor){
        DoctorModel doctor = this.findById(id);
        doctor.setSpecialty(updateddoctor.getSpecialty());

        return this.doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long id){
        this.doctorRepository.deleteById(id);
    }

    public DoctorModel findDoctorByCRM(Long crm) {
        return this.doctorRepository.findDoctorByCRM(crm);
    }
}

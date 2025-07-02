package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.model.DoctorHistoryModel;
import br.com.ControleDePacientes.repository.DoctorHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorHistoryService {
    @Autowired private DoctorHistoryRepository doctorHistoryRepository;

    @Transactional
    public DoctorHistoryModel saveDoctorHistory(DoctorHistoryModel history){
        return this.doctorHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    public List<DoctorHistoryModel> listDoctorsHistory(){
        return this.doctorHistoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DoctorHistoryModel findById(Long id){
        return doctorHistoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Histórico não encontrado."));
    }

    @Transactional
    public DoctorHistoryModel updateDoctorHistory(DoctorHistoryModel updatehistory){
        updatehistory.setEndTime(LocalDateTime.now());
        return this.doctorHistoryRepository.save(updatehistory);
    }

    @Transactional
    public void deleteDoctorHistory(Long id){
        this.doctorHistoryRepository.deleteById(id);
    }

    public Optional<DoctorHistoryModel> findActiveByAdmission(Long id) {
        return Optional.ofNullable(this.doctorHistoryRepository.findActiveByAdmission(id));
    }
}

package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.model.DoctorLogModel;
import br.com.ControleDePacientes.repository.DoctorLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorLogService {
    @Autowired private DoctorLogRepository doctorLogRepository;

    @Transactional
    public DoctorLogModel saveDoctorLog(DoctorLogModel log){
        return this.doctorLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<DoctorLogModel> listDoctorsLog(){
        return this.doctorLogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DoctorLogModel findById(Long id){
        return this.doctorLogRepository.findById(id).orElseThrow(() -> new RuntimeException("Histórico não encontrado."));
    }

    @Transactional
    public DoctorLogModel finishDoctorLog(DoctorLogModel finishlog){
        finishlog.setEndTime(LocalDateTime.now());
        return this.doctorLogRepository.save(finishlog);
    }

    public Optional<DoctorLogModel> findActiveByAdmission(Long id) {
        return Optional.ofNullable(this.doctorLogRepository.findActiveByAdmission(id));
    }
}

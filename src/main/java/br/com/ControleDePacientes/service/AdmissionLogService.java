package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.*;
import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.model.*;
import br.com.ControleDePacientes.projections.LogProjection;
import br.com.ControleDePacientes.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdmissionLogService {

    @Autowired private PatientService patientService;
    @Autowired private BedService bedService;
    @Autowired private DoctorService doctorService;
    @Autowired private DoctorLogService doctorLogService;
    @Autowired private AdmissionLogRepository admissionLogRepository;

    @Transactional
    public AdmissionResponseDTO admitPatient(AdmissionRequestDTO admissionRequest){
        if (admissionRequest.getPatientId() == null || admissionRequest.getBedId() == null || admissionRequest.getDoctorId() == null) {
            throw new IllegalArgumentException("ID do Paciente, Id do Leito e Id do Médico são obrigatórios.");
        }

        //Verifica se o paciente já está internado
        this.admissionLogRepository.findActiveAdmissionByPatientId(admissionRequest.getPatientId())
                .ifPresent(admission -> {
                    throw new IllegalStateException("Paciente já possui uma internação ativa.");
                });

        //Busca o paciente, leito e médico para internação (logo abaixo busca o leito também)
        PatientModel patient = this.patientService.findById(admissionRequest.getPatientId());
        BedModel bed = this.bedService.findById(admissionRequest.getBedId());
        DoctorModel doctor = this.doctorService.findById(admissionRequest.getDoctorId());

        if (bed.getStatus() != BedStatus.AVAILABLE) {
            throw new IllegalStateException("O leito " + bed.getCode() + " não está disponível.");
        }

        bed.setPatient(patient);
        bed.setStatus(BedStatus.OCCUPIED);
        this.bedService.save(bed);

        AdmissionLogModel admissionLog = new AdmissionLogModel();
        admissionLog.setPatient(patient);
        admissionLog.setBed(bed);
        admissionLog.setActiveDoctor(doctor);
        admissionLog.setAdmissionDate(LocalDateTime.now());
        admissionLog.setDischargeDate(null);
        AdmissionLogModel savedLog = this.admissionLogRepository.save(admissionLog);


        // Histórico do primeiro médico responsável
        DoctorLogModel history = new DoctorLogModel();
        history.setAdmission(savedLog);
        history.setDoctor(doctor);
        history.setStartTime(LocalDateTime.now());
        this.doctorLogService.saveDoctorLog(history);

        return new AdmissionResponseDTO(savedLog);
    }

    //Dar alta
    @Transactional
    public AdmissionResponseDTO dischargePatient(Long patientId){
        AdmissionLogModel activeAdmission = this.admissionLogRepository.findActiveAdmissionByPatientId(patientId).orElseThrow(() -> new RuntimeException("Internação não encontrada"));

        activeAdmission.setDischargeDate(LocalDateTime.now());
        AdmissionLogModel updatedAdmissionLog = this.admissionLogRepository.save(activeAdmission);

        BedModel bed = activeAdmission.getBed();
        bed.setPatient(null);
        bed.setStatus(BedStatus.AVAILABLE);
        this.bedService.save(bed);

        return new AdmissionResponseDTO(updatedAdmissionLog);
    }

    @Transactional(readOnly = true)
    public List<LogDTO> getCurrentlyAdmittedPatients(){
        return this.admissionLogRepository.findActiveAdmissions()
                .stream().map(LogDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<LogDTO> getAdmissionHistoryByPatientId(Long patientId, Pageable pageable){
        Page<LogProjection> page = this.admissionLogRepository.findAdmissionHistoryByPatientId(patientId, pageable);
        return page.map(LogDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<BedHistoryDTO> getBedAdmissionHistory(Long bedId, Pageable pageable){
        return this.admissionLogRepository.findBedAdmissionHistoryById(bedId, pageable);
    }

    //Muda o doutor responsável pela internação
    public AdmissionResponseDTO changeActiveDoctor(Long admissionId, Long doctorId) {
        //Busca a log e o Doutor
        DoctorModel doctor = this.doctorService.findById(doctorId);
        AdmissionLogModel admission = this.admissionLogRepository.findById(admissionId).orElseThrow(() -> new RuntimeException("Internação não encontrada."));

        //Verifica se a internação está ativa
        if (admission.getDischargeDate() != null) {
            throw new IllegalArgumentException("Não é possível alterar o médico responsável em uma internação já encerrada.");
        }

        // Encerrar o histórico atual
        DoctorLogModel currentHistory =  this.doctorLogService.findActiveByAdmission(admission.getId())
                .orElseThrow(() -> new IllegalStateException("Histórico atual do médico não encontrado."));
        this.doctorLogService.finishDoctorLog(currentHistory);

        // Cria um novo histórico com novo médico
        DoctorLogModel newHistory = new DoctorLogModel();
        newHistory.setAdmission(admission);
        newHistory.setDoctor(doctor);
        newHistory.setStartTime(LocalDateTime.now());
        this.doctorLogService.saveDoctorLog(newHistory);

        //Atualiza o doutor na log.
        admission.setActiveDoctor(doctor);
        return new AdmissionResponseDTO(this.admissionLogRepository.save(admission));
    }
}

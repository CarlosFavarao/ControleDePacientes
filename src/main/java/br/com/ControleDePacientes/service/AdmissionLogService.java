package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.*;
import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.model.*;
import br.com.ControleDePacientes.projections.LogProjection;
import br.com.ControleDePacientes.repository.*;
import jakarta.persistence.EntityNotFoundException;
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

    @Autowired private PatientRepository patientRepository;
    @Autowired private WardRepository wardRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private BedRepository bedRepository;
    @Autowired private AdmissionLogRepository admissionLogRepository;

    @Transactional
    public AdmissionResponseDTO admitPatient(AdmissionRequestDTO admissionRequest){
        if (admissionRequest.getPatientId() == null || admissionRequest.getBedId() == null) {
            throw new IllegalArgumentException("ID do Paciente e Id do Leito são obrigatórios.");
        }

        //Verifica se o paciente já está internado
        admissionLogRepository.findActiveAdmissionByPatientId(admissionRequest.getPatientId())
                .ifPresent(admission -> {
                    throw new IllegalStateException("Paciente já possui uma internação ativa.");
                });

        //Busca o paciente o leito para internação
        PatientModel patient = patientRepository.findById(admissionRequest.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com ID: " + admissionRequest.getPatientId()));

        BedModel bed = bedRepository.findById(admissionRequest.getBedId())
                .orElseThrow(() -> new EntityNotFoundException("Leito não encontrado com ID: " + admissionRequest.getBedId()));

        if (bed.getStatus() != BedStatus.AVAILABLE) {
            throw new IllegalStateException("O leito " + bed.getCode() + " não está disponível.");
        }

        bed.setPatient(patient);
        bed.setStatus(BedStatus.OCCUPIED);
        bedRepository.save(bed);

        AdmissionLogModel admissionLog = new AdmissionLogModel();
        admissionLog.setPatient(patient);
        admissionLog.setBed(bed);
        admissionLog.setAdmissionDate(LocalDateTime.now());
        admissionLog.setDischargeDate(null);

        AdmissionLogModel savedLog = admissionLogRepository.save(admissionLog);
        return new AdmissionResponseDTO(savedLog);
    }


    //Dar alta
    @Transactional
    public AdmissionLogModel dischargePatient(Long patientId){
        AdmissionLogModel activeAdmission = admissionLogRepository.findActiveAdmissionByPatientId(patientId).orElseThrow(() -> new RuntimeException("Internação não encontrada"));

        activeAdmission.setDischargeDate(LocalDateTime.now());
        AdmissionLogModel updatedAdmissionLog = admissionLogRepository.save(activeAdmission);

        BedModel bed = activeAdmission.getBed();
        bed.setPatient(null);
        bed.setStatus(BedStatus.AVAILABLE);
        bedRepository.save(bed);

        return updatedAdmissionLog;
    }

    @Transactional(readOnly = true)
    public List<LogDTO> getCurrentlyAdmittedPatients(){
        return this.admissionLogRepository.findActiveAdmissions()
                .stream().map(LogDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<LogDTO> getAdmissionHistoryByPatientId(Long patientId, Pageable pageable){
        Page<LogProjection> page = admissionLogRepository.findAdmissionHistoryByPatientId(patientId, pageable);
        return page.map(LogDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<BedHistoryDTO> getBedAdmissionHistory(Long bedId, Pageable pageable){
        return this.admissionLogRepository.findBedAdmissionHistoryById(bedId, pageable);
    }
}

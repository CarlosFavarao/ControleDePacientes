package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.*;
import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.model.*;
import br.com.ControleDePacientes.repository.AdmissionLogRepository;
import br.com.ControleDePacientes.repository.VisitsLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitsLogService {
    @Autowired
    private PatientService patientService;
    @Autowired
    private BedService bedService;
    @Autowired
    private VisitsLogRepository visitsLogRepository;

    private VisitorService visitorService;

    @Autowired
    public void setVisitorService(@Lazy VisitorService visitorService) {
        this.visitorService = visitorService;
    }


    @Transactional(readOnly = true)
    public VisitsLogModel findByVisitorId(Long patientId) { //Encontrar log pelo id do visitante
        return this.visitsLogRepository.findByVisitorId(patientId);
    }

    @Transactional
    public VisitsLogResponseDTO registerVisit(VisitsLogRequestDTO visitsLogRequestDTO) {
        if (visitsLogRequestDTO.getPatientId() == null || visitsLogRequestDTO.getVisitorId() == null) {
            throw new IllegalArgumentException("ID do Paciente e Id do Visitante são obrigatórios.");
        }

        //Verifica se o paciente já está com visita ativa
        this.visitsLogRepository.findActiveVisitByPatientId(visitsLogRequestDTO.getPatientId())
                .ifPresent(visit -> {
                    throw new IllegalStateException("Paciente já possui uma visita em andamento.");
                });

        //Busca se o visitante está visitando algum outro paciente
        this.visitsLogRepository.findActiveVisitByVisitorId(visitsLogRequestDTO.getVisitorId())
                .ifPresent(visit -> {
                    throw new IllegalStateException("Visitante já está visitando outro paciente.");
                });

        //Busca o paciente com internação ativa e o visitante para registrar a visita
        VisitorModel visitor = visitorService.findById(visitsLogRequestDTO.getVisitorId());
        PatientModel patient = patientService.findById(visitsLogRequestDTO.getPatientId());

        if (bedService.findActiveBedByPatientId(patient.getId()).isEmpty()) {
            throw new IllegalArgumentException("Paciente não possui internação ativa.");
        }

        VisitsLogModel visitsLog = new VisitsLogModel();
        visitsLog.setEntryDate(LocalDateTime.now());
        visitsLog.setExitDate(null); // A data de saída será definida quando a visita for finalizada
        visitsLog.setPatient(patient);
        visitsLog.setVisitor(visitor);

        return new VisitsLogResponseDTO(this.visitsLogRepository.save(visitsLog));

    }

    @Transactional
    public VisitsLogResponseDTO unregisterVisit(Long visitorId){
        VisitsLogModel activeVisit = this.visitsLogRepository.findActiveVisitByVisitorId(visitorId).orElseThrow(() -> new RuntimeException("Visitante ativo não encontrado"));

        activeVisit.setExitDate(LocalDateTime.now());
        VisitsLogModel updatedVisitLog = this.visitsLogRepository.save(activeVisit);

        return new VisitsLogResponseDTO(updatedVisitLog);
    }

    @Transactional(readOnly = true)
    public List<VisitsLogResponseDTO> getCurrentlyVisitedPatients(){
        return this.visitsLogRepository.findAllActiveVisits()
                .stream().map(VisitsLogResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisitsLogResponseDTO> getClosedVisits(){
        return this.visitsLogRepository.findAllClosedVisits()
                .stream().map(VisitsLogResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisitsLogResponseDTO> getVisitByVisitorId(Long visitorId) { //Encontrar log pelo id do visitante
        return this.visitsLogRepository.getVisitByVisitorId(visitorId)
                .stream().map(VisitsLogResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisitsLogResponseDTO> getVisitByPatientId(Long patientId) { //Encontrar log pelo id do visitante
        return this.visitsLogRepository.getVisitByPatientId(patientId)
                .stream().map(VisitsLogResponseDTO::new).collect(Collectors.toList());
    }
}

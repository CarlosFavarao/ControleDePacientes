package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.*;
import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.enums.LogStatus;
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
    @Autowired private DoctorHistoryService doctorHistoryService;
    @Autowired private AdmissionLogRepository admissionLogRepository;

    @Transactional
    public AdmissionResponseDTO admitPatient(AdmissionRequestDTO admissionRequest){
        if (admissionRequest.getPatientId() == null || admissionRequest.getBedId() == null || admissionRequest.getDoctorId() == null) {
            throw new IllegalArgumentException("ID do Paciente, Id do Leito e Id do Médico são obrigatórios.");
        }

        this.findActiveAdmissionByPatientId(admissionRequest.getPatientId(), false);

        PatientModel patient = this.patientService.findById(admissionRequest.getPatientId());
        BedModel bed = this.bedService.findById(admissionRequest.getBedId());
        DoctorModel doctor = this.doctorService.findById(admissionRequest.getDoctorId());

        BedModel admissionBed = setAdmissionBedInfo(bed, patient);
        this.bedService.save(admissionBed);

        AdmissionLogModel admissionLog = setAdmissionLogInfo(new AdmissionLogModel(), patient, admissionBed, doctor);
        AdmissionLogModel savedLog = this.admissionLogRepository.save(admissionLog);

        // Histórico do primeiro médico responsável
        DoctorHistoryModel history = new DoctorHistoryModel();
        history.setAdmission(savedLog);
        history.setDoctor(doctor);
        history.setStartTime(LocalDateTime.now());
        this.doctorHistoryService.saveDoctorHistory(history);

        return new AdmissionResponseDTO(savedLog);
    }

    @Transactional
    public AdmissionResponseDTO dischargePatient(Long patientId){
        AdmissionLogModel activeAdmission = this.findActiveAdmissionByPatientId(patientId, true);

        AdmissionLogModel updatedAdmissionLog = this.save(setDischargeLogInfo(activeAdmission));
        setDischargeBedInfo(activeAdmission.getBed());

        return new AdmissionResponseDTO(updatedAdmissionLog);
    }

    @Transactional
    public AdmissionResponseDTO transferPatient(TransferPatientDTO transferPatient){
        AdmissionLogModel activeAdmission = this.findActiveAdmissionByPatientId(transferPatient.getPatientId(), true);

        BedModel nextBed = bedService.findById(transferPatient.getNewBedId());

        if (nextBed.getStatus() != BedStatus.AVAILABLE) {
            throw new IllegalStateException("O leito " + nextBed.getCode() + " não está disponível. ");
        }

        AdmissionLogModel newAdmission = this.setTransferLogInfo(activeAdmission, new AdmissionLogModel(), nextBed);

        this.setTransferDoctor(transferPatient, activeAdmission, newAdmission);

        this.save(newAdmission);
        return new AdmissionResponseDTO(newAdmission);
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
    public AdmissionResponseDTO changeDoctor(Long admissionId, Long doctorId) {
        //Busca a log e o Doutor
        DoctorModel doctor = this.doctorService.findById(doctorId);
        AdmissionLogModel admission = this.admissionLogRepository.findById(admissionId).orElseThrow(() -> new RuntimeException("Internação não encontrada."));

        //Verifica se a internação está ativa
        if (admission.getDischargeDate() != null) {
            throw new IllegalArgumentException("Não é possível alterar o médico responsável em uma internação já encerrada.");
        }

        // Encerrar o histórico atual
        DoctorHistoryModel currentHistory =  this.doctorHistoryService.findActiveByAdmission(admission.getId())
                .orElseThrow(() -> new IllegalStateException("Histórico atual do médico não encontrado."));
        this.doctorHistoryService.updateDoctorHistory(currentHistory);

        // Cria um novo histórico com novo médico
        DoctorHistoryModel newHistory = new DoctorHistoryModel();
        newHistory.setAdmission(admission);
        newHistory.setDoctor(doctor);
        newHistory.setStartTime(LocalDateTime.now());
        this.doctorHistoryService.saveDoctorHistory(newHistory);

        //Atualiza o doutor na log.
        admission.setDoctor(doctor);
        return new AdmissionResponseDTO(this.admissionLogRepository.save(admission));
    }

    private AdmissionLogModel save(AdmissionLogModel admissionLog){
        return this.admissionLogRepository.save(admissionLog);
    }

    private AdmissionLogModel findActiveAdmissionByPatientId(Long patientId, boolean hasToExist){
        if (hasToExist) {
            return this.admissionLogRepository.findActiveAdmissionByPatientId(patientId)
                    .orElseThrow(() -> new RuntimeException("Internação não encontrada."));
        }else{
            this.admissionLogRepository.findActiveAdmissionByPatientId(patientId)
                    .ifPresent(a -> {
                        throw new IllegalStateException("Paciente já possui uma internação ativa.");
                    });
            return null;
        }
    }

    private AdmissionLogModel setAdmissionLogInfo(AdmissionLogModel admissionLog, PatientModel patient, BedModel bed, DoctorModel doctor){
        admissionLog.setPatient(patient);
        admissionLog.setBed(bed);
        admissionLog.setStatus(LogStatus.INTERNADO);
        admissionLog.setDoctor(doctor);
        admissionLog.setAdmissionDate(LocalDateTime.now());
        admissionLog.setDischargeDate(null);

        return admissionLog;
    }

    private BedModel setAdmissionBedInfo(BedModel bed, PatientModel patient){
        if (bed.getStatus() != BedStatus.AVAILABLE) {
            throw new IllegalStateException("O leito " + bed.getCode() + " não está disponível.");
        }

        bed.setPatient(patient);
        bed.setStatus(BedStatus.OCCUPIED);
        return bed;
    }

    private AdmissionLogModel setDischargeLogInfo(AdmissionLogModel admissionLog){
        admissionLog.setDischargeDate(LocalDateTime.now());
        admissionLog.setStatus(LogStatus.ALTA);
        return this.save(admissionLog);
    }

    private void setDischargeBedInfo(BedModel bed){
        bed.setPatient(null);
        bed.setStatus(BedStatus.AVAILABLE);
        this.bedService.save(bed);
    }

    private AdmissionLogModel setTransferLogInfo(AdmissionLogModel activeAdmission, AdmissionLogModel newAdmission, BedModel nextBed){
        activeAdmission.setDischargeDate(LocalDateTime.now());
        activeAdmission.setStatus(LogStatus.TRANSFERIDO);
        activeAdmission.setMoved_to(nextBed);
        admissionLogRepository.save(activeAdmission);

        BedModel bed = activeAdmission.getBed();
        bed.setPatient(null);
        bed.setStatus(BedStatus.AVAILABLE);
        bedService.save(bed);

        newAdmission.setPatient(activeAdmission.getPatient());
        newAdmission.setStatus(LogStatus.INTERNADO);
        newAdmission.setBed(nextBed);
        newAdmission.setAdmissionDate(LocalDateTime.now());
        newAdmission.setDischargeDate(null);

        nextBed.setStatus(BedStatus.OCCUPIED);
        nextBed.setPatient(activeAdmission.getPatient());

        return newAdmission;
    }

    private void setTransferDoctor(TransferPatientDTO transferPatient, AdmissionLogModel activeAdmission, AdmissionLogModel newAdmission){
        boolean theresChange = bedService.findWardByBedId(activeAdmission.getBed().getId())
                .equals(bedService.findWardByBedId(newAdmission.getBed().getId()));

        if (theresChange) {
            if (transferPatient.getDoctor() != null) {
                newAdmission.setDoctor(transferPatient.getDoctor());
            } else {
                newAdmission.setDoctor(activeAdmission.getDoctor());
            }
        } else {
            if (transferPatient.getDoctor() == null) {
                throw new IllegalStateException("É obrigatório informar o médico responsável para transferir");
            }
            newAdmission.setDoctor(transferPatient.getDoctor());
        }
    }
}

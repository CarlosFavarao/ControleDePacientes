package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.admission.AdmissionRequestDTO;
import br.com.ControleDePacientes.dto.admission.AdmissionResponseDTO;
import br.com.ControleDePacientes.dto.admission.AdmittedPatientDTO;
import br.com.ControleDePacientes.dto.patient.PatientLocationDTO;
import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.enums.SpecialtyEnum;
import br.com.ControleDePacientes.model.*;
import br.com.ControleDePacientes.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AdmissionService {

    @Autowired private PatientRepository patientRepository;
    @Autowired private WardRepository wardRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private BedRepository bedRepository;
    @Autowired private AdmissionLogRepository admissionLogRepository;

    //Admitir um paciente (vai ser alterado futuramente, mas funciona)
    @Transactional
    public AdmissionResponseDTO admitPatient(AdmissionRequestDTO admissionRequest){
        if (admissionRequest.getPatientId() == null || admissionRequest.getSpecialty() == null) {
            throw new IllegalArgumentException("ID do Paciente e Especialidade são obrigatórios.");
        }

        PatientModel patient = patientRepository.findById(admissionRequest.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado..."));

        List<WardModel> wardsWithSpecialty = wardRepository.findAll().stream()
                .filter(ward -> ward.getSpecialty() == admissionRequest.getSpecialty()).toList();

        if (wardsWithSpecialty.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma ala encontrada para a especialidade: " + admissionRequest.getSpecialty());
        }

        BedModel availableBed = null;
        for (WardModel ward : wardsWithSpecialty) {
            List<RoomModel> roomsInWard = roomRepository.findAll().stream()
                    .filter(room -> room.getWard().getId().equals(ward.getId()))
                    .toList();

            for (RoomModel room : roomsInWard) {
                Optional<BedModel> bedOptional = bedRepository.findAll().stream()
                        .filter(bed -> bed.getRoom().getId().equals(room.getId()) && bed.getStatus() == BedStatus.AVAILABLE)
                        .findFirst();

                if (bedOptional.isPresent()) {
                    availableBed = bedOptional.get();
                    break;
                }
            }
            if (availableBed != null) {
                break;
            }
        }

        if (availableBed == null) {
            throw new RuntimeException("Nenhum leito disponível para a especialidade: " + admissionRequest.getSpecialty());
        }

        availableBed.setPatient(patient);
        availableBed.setStatus(BedStatus.OCCUPIED);
        bedRepository.save(availableBed);

        AdmissionLogModel admissionLog = new AdmissionLogModel();
        admissionLog.setPatient(patient);
        admissionLog.setBed(availableBed);
        admissionLog.setAdmissionDate(LocalDateTime.now());
        admissionLog.setDischargeDate(null);

        AdmissionLogModel savedLog = admissionLogRepository.save(admissionLog);
        return new AdmissionResponseDTO(savedLog); // << MUDANÇA AQUI
    }

    public Optional<PatientLocationDTO> findPatientLocation(Long patientId) { //Encontrar paciente
        return admissionLogRepository.findPatientLocationDetails(patientId);
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

    //Retornar todos os pacientes internados, em ordem alfabética e agrupados por especialidade.
    public Map<SpecialtyEnum, List<AdmittedPatientDTO>> getCurrentlyAdmittedPatients(){
        List<AdmissionLogModel> activeAdmissions = admissionLogRepository.findActiveAdmissions();

        //Árvore para deixar especialidades já ordenadas
        Map<SpecialtyEnum, List<AdmittedPatientDTO>> reportMap = new TreeMap<>();

        for (AdmissionLogModel log : activeAdmissions){
            String patientName = log.getPatient().getName();
            SpecialtyEnum specialty = log.getBed().getRoom().getWard().getSpecialty();
            LocalDateTime admissionDate = log.getAdmissionDate();
            long daysAdmitted = ChronoUnit.DAYS.between(admissionDate,LocalDateTime.now());

            AdmittedPatientDTO dto = new AdmittedPatientDTO(patientName, specialty, admissionDate, daysAdmitted);

            //Esse compute if absent garante que seja criado, mesmo que seja a primeira vez por especialidade
            reportMap.computeIfAbsent(specialty, k -> new ArrayList<>()).add(dto);
        }

        for (List<AdmittedPatientDTO> patientDTOList : reportMap.values()){
            patientDTOList.sort(Comparator.comparing(AdmittedPatientDTO::getPatientName));
        }

        return reportMap;
    }
}

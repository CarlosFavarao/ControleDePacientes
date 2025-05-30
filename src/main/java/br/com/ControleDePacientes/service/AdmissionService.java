package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.AdmissionRequestDTO;
import br.com.ControleDePacientes.dto.PatientLocationDTO;
import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.model.*;
import br.com.ControleDePacientes.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdmissionService {

    @Autowired private PatientRepository patientRepository;
    @Autowired private WardRepository wardRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private BedRepository bedRepository;
    @Autowired private AdmissionLogRepository admissionLogRepository;

    //Admitir um paciente (vai ser alterado futuramente, mas funciona)
    @Transactional
    public AdmissionLogModel admitPatient(AdmissionRequestDTO admissionRequest){
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

        return admissionLogRepository.save(admissionLog);
    }

    @Transactional(readOnly = true)
    public Optional<PatientLocationDTO> findPatientLocation(Long patientId) {
        return admissionLogRepository.findPatientLocationDetails(patientId);
    }

}

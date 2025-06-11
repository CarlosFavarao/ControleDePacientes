package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.SpecialtyBedStatsDTO;
import br.com.ControleDePacientes.dto.SpecialtyRoomStatsDTO;
import br.com.ControleDePacientes.dto.WardCreateRequestDTO;
import br.com.ControleDePacientes.dto.WardResponseDTO;
import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.enums.RoomStatus;
import br.com.ControleDePacientes.enums.SpecialtyEnum;
import br.com.ControleDePacientes.model.BedModel;
import br.com.ControleDePacientes.model.HospitalModel;
import br.com.ControleDePacientes.model.RoomModel;
import br.com.ControleDePacientes.model.WardModel;
import br.com.ControleDePacientes.repository.BedRepository;
import br.com.ControleDePacientes.repository.HospitalRepository;
import br.com.ControleDePacientes.repository.RoomRepository;
import br.com.ControleDePacientes.repository.WardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WardService {
    @Autowired private WardRepository wardRepository;
    @Autowired private HospitalRepository hospitalRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private BedRepository bedRepository;

    @Transactional
    public WardModel createWardWithRoomsAndBeds(WardCreateRequestDTO dto){ //Cria as Alas com Quartos e leitos prontos, funciona bem porém...
        if (dto.getHospitalId() == null                                    //É carregado e complexo, foi o que deu para fazer... Bem legal a lógica de tudo.
                || dto.getSpecialty() == null
                || dto.getNumberOfRooms() <= 0
                || dto.getBedsPerRoom() <= 0){
            throw new IllegalArgumentException("Dados inválidos para a criação da Ala.");
        }

        HospitalModel hospital = this.hospitalRepository.findById(dto.getHospitalId())
                .orElseThrow(() -> new EntityNotFoundException("Hospital não encontrado."));

        WardModel newWard = new WardModel();
        newWard.setSpecialty(dto.getSpecialty());
        newWard.setHospital(hospital);
        WardModel savedWard = wardRepository.save(newWard);

        String prefix = savedWard.getSpecialty().getPrefix(); //Prefixo para nomenclatura
        String specialtyName = savedWard.getSpecialty().name(); //Nome da especialidade para consulta no BD

        List<Integer> existingNumbersList = roomRepository.findExistingRoomNumbers(hospital.getId(), specialtyName);

        Set<Integer> existingNumbersSet = new HashSet<>(existingNumbersList); //Pega os números resgatados e coloca-os em um hashset, para uma busca rápida e eficaz

        int nextRoomNumber = 1;
        for (int i = 0; i < dto.getNumberOfRooms(); i++) {
            while (existingNumbersSet.contains(nextRoomNumber)) { //Encontra o próximo número vago
                nextRoomNumber++;
            }

            //Cria o quarto baseado nesse número resgatado lá em cima
            RoomModel newRoom = new RoomModel();
            String roomCode = prefix + nextRoomNumber;
            newRoom.setCode(roomCode);
            newRoom.setStatus(RoomStatus.ACTIVE);
            newRoom.setWard(savedWard);
            RoomModel savedRoom = roomRepository.save(newRoom);

            //Adiciona no HashSet (para não ser adicionado o mesmo várias vezes)
            existingNumbersSet.add(nextRoomNumber);

            //Cria os leitos normalmente.
            for (int j = 1; j <= dto.getBedsPerRoom(); j++) {
                BedModel newBed = new BedModel();
                String bedCode = roomCode + "-" + j;
                newBed.setCode(bedCode);
                newBed.setStatus(BedStatus.AVAILABLE);
                newBed.setRoom(savedRoom);
                newBed.setPatient(null);
                bedRepository.save(newBed);
            }
        }
        return savedWard;
    }

    public List<WardResponseDTO> findAllWards() {
        List<WardModel> wards = wardRepository.findAll();
        return wards.stream().map(WardResponseDTO::new).collect(Collectors.toList());
    }

    public Optional<WardResponseDTO> findWardById(Long id) {
        return wardRepository.findById(id).map(WardResponseDTO::new);
    }

    public List<SpecialtyBedStatsDTO> getBedStatsBySpecialty(){
        return wardRepository.getBedStatsBySpecialty();
    }

    public boolean existsByHospitalId(Long hospitalId){
        return wardRepository.existsByHospitalId(hospitalId);
    }

    public List<SpecialtyRoomStatsDTO> getRoomStatsBySpecialty() {
        List<WardModel> wardsWithDetails = wardRepository.findAllWithRoomsAndBeds();
        Map<SpecialtyEnum, RoomStatsAggregator> statsMap = new HashMap<>();

        for (WardModel ward : wardsWithDetails) {
            SpecialtyEnum specialty = ward.getSpecialty();
            RoomStatsAggregator aggregator = statsMap.computeIfAbsent(specialty, k -> new RoomStatsAggregator());

            for (RoomModel room : ward.getRooms()) {
                aggregator.totalRooms++;

                boolean isRoomOccupiedByPatient = false;
                if (room.getBeds() != null && !room.getBeds().isEmpty()) {
                    for (BedModel bed : room.getBeds()) {
                        if (bed.getStatus() == BedStatus.OCCUPIED) { //
                            isRoomOccupiedByPatient = true;
                            break;
                        }
                    }
                }

                if (isRoomOccupiedByPatient) {
                    aggregator.occupiedRooms++;
                } else {
                    aggregator.freeRooms++;
                }
            }
        }

        List<SpecialtyRoomStatsDTO> result = new ArrayList<>();
        for (Map.Entry<SpecialtyEnum, RoomStatsAggregator> entry : statsMap.entrySet()) {
            result.add(new SpecialtyRoomStatsDTO(
                    entry.getKey(),
                    entry.getValue().totalRooms,
                    entry.getValue().freeRooms,
                    entry.getValue().occupiedRooms
            ));
        }
        return result;
    }

    private static class RoomStatsAggregator {
        long totalRooms = 0;
        long freeRooms = 0;
        long occupiedRooms = 0;
    }


}
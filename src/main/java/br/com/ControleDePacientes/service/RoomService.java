package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.AvailableRoomDTO;
import br.com.ControleDePacientes.dto.RoomResponseDTO;
import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.enums.RoomStatus;
import br.com.ControleDePacientes.model.RoomModel;
import br.com.ControleDePacientes.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Transactional
    public RoomModel save(RoomModel room){
        return this.roomRepository.save(room);
    }

    @Transactional
    public RoomResponseDTO updateRoom(Long id){
        RoomModel room = this.roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Quarto não encontrado."));

        if (room.getStatus() == RoomStatus.ACTIVE) {
            room.setStatus(RoomStatus.MAINTENANCE);
        } else {
            room.setStatus(RoomStatus.ACTIVE);
        }

        RoomModel updatedRoom = this.roomRepository.save(room);

        return new RoomResponseDTO(updatedRoom);
    }

    @Transactional(readOnly = true)
    public List<RoomResponseDTO> findAllRooms(){
        return this.roomRepository.findAll().stream().map(RoomResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomResponseDTO findRoomById(Long id){
        return this.roomRepository.findById(id).map(RoomResponseDTO::new)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado."));
    }

    @Transactional
    public List<Integer> findExistingRoomNumbers(Long hospitalId, String specialtyName){
        return this.roomRepository.findExistingRoomNumbers(hospitalId,specialtyName);
    }

    @Transactional(readOnly = true)
    public List<AvailableRoomDTO> findRoomsWithAvailableBeds(){
        return this.roomRepository.findRoomsWithBedsByStatus(BedStatus.AVAILABLE); //Posso procurar aonde não tem disponível também,
    }                                                                              //Mesmo não sendo muito útil agora
}

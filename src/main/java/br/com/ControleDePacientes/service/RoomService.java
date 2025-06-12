package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.AvailableRoomDTO;
import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.model.RoomModel;
import br.com.ControleDePacientes.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Transactional
    public RoomModel save(RoomModel room){
        return roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public List<RoomModel> findAllRooms(){
        return this.roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RoomModel findRoomById(Long id){
        return this.roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Quarto não encontrado."));
    }

    @Transactional
    public List<Integer> findExistingRoomNumbers(Long hospitalId, String specialtyName){
        return this.roomRepository.findExistingRoomNumbers(hospitalId,specialtyName);
    }

    @Transactional(readOnly = true)
    public List<AvailableRoomDTO> findRoomsWithAvailableBeds(){
        return this.roomRepository.findRoomsWithBedsByStatus(BedStatus.AVAILABLE); //Posso procurar aonde não tem disponível também,
    }                                                                         //Mesmo não sendo muito útil agora
}

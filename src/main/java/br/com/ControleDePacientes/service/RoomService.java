package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.AvailableRoomDTO;
import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.model.RoomModel;
import br.com.ControleDePacientes.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public List<RoomModel> findAllRooms(){
        return roomRepository.findAll();
    }

    public Optional<RoomModel> findRoomById(Long id){
        return roomRepository.findById(id);
    }

    public List<AvailableRoomDTO> findRoomsWithAvailableBeds(){
        return roomRepository.findRoomsWithBedsByStatus(BedStatus.AVAILABLE); //Posso procurar aonde não tem disponivel também,
    }                                                                         //Mesmo não sendo muito útil
}

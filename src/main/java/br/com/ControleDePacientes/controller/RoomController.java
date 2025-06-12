package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.AvailableRoomDTO;
import br.com.ControleDePacientes.model.RoomModel;
import br.com.ControleDePacientes.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping
    public List<RoomModel> findAllRooms(){
        return this.roomService.findAllRooms();
    }

    @GetMapping("/{id}")
    public ResponseEntity findByRoomId(@PathVariable Long id){
        return ResponseEntity.ok(this.roomService.findRoomById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableRoomDTO>> getAvailableRooms(){
        List<AvailableRoomDTO> availableRooms = this.roomService.findRoomsWithAvailableBeds();
        return ResponseEntity.ok(availableRooms);
    }
}

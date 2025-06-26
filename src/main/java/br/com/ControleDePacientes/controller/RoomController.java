package br.com.ControleDePacientes.controller;

import br.com.ControleDePacientes.dto.AvailableRoomDTO;
import br.com.ControleDePacientes.dto.RoomResponseDTO;
import br.com.ControleDePacientes.model.RoomModel;
import br.com.ControleDePacientes.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PutMapping("/{id}/update")
    public ResponseEntity updateRoom(@PathVariable Long id){
        return ResponseEntity.ok(this.roomService.updateRoom(id));
    }

    @GetMapping
    public List<RoomResponseDTO> findAllRooms(){
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

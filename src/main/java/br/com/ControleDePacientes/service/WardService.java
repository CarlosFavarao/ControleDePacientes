package br.com.ControleDePacientes.service;

import br.com.ControleDePacientes.dto.SpecialtyBedStatsDTO;
import br.com.ControleDePacientes.dto.WardCreateRequestDTO;
import br.com.ControleDePacientes.enums.BedStatus;
import br.com.ControleDePacientes.enums.RoomStatus;
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
import java.util.ArrayList;
import java.util.List;

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

        HospitalModel hospital = hospitalRepository.findById(dto.getHospitalId())
                .orElseThrow(() -> new EntityNotFoundException("Hospital não encontrado."));

        WardModel newWard = new WardModel();
        newWard.setSpecialty(dto.getSpecialty());
        newWard.setHospital(hospital);
        WardModel savedWard = wardRepository.save(newWard);

        String prefix = savedWard.getSpecialty().getPrefix();
        List<RoomModel> createdRooms = new ArrayList<>();
        List<BedModel> createdBeds = new ArrayList<>();

        for (int i = 1; i <= dto.getNumberOfRooms(); i++){ //Cria os quartos
            RoomModel newRoom = new RoomModel();
            String roomCode = prefix + i; //Para o código ser gerado já com o Prefixo definido...
            newRoom.setCode(roomCode);
            newRoom.setStatus(RoomStatus.ACTIVE);
            newRoom.setWard(savedWard);
            RoomModel savedRoom = roomRepository.save(newRoom);

            for (int j = 1; j <= dto.getBedsPerRoom(); j++){ //Cria as camas nos quartos
                BedModel newBed = new BedModel();
                String bedCode = roomCode + "-" + j; //Gera o código da cama pelo código da sala
                newBed.setCode(bedCode);
                newBed.setStatus(BedStatus.AVAILABLE);
                newBed.setRoom(savedRoom);
                newBed.setPatient(null); //Null = sem paciente
                createdBeds.add(bedRepository.save(newBed));
            }
        }
        return savedWard;
    }

    @Transactional
    public List<WardModel> findAllWards() {
        return wardRepository.findAll();
    }

    @Transactional //LEmbrar de importar o do Spring e não o do Jakarta
    public WardModel findWardById(Long id) {
        return wardRepository.findById(id).orElseThrow(() -> new RuntimeException("Ala não encontrada."));
    }

    public List<SpecialtyBedStatsDTO> getBedStatsBySpecialty(){
        return wardRepository.getBedStatsBySpecialty();
    }

}
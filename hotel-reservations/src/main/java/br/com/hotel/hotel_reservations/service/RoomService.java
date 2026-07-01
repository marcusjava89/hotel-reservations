package br.com.hotel.hotel_reservations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.hotel.hotel_reservations.dto.RoomRequestDTO;
import br.com.hotel.hotel_reservations.dto.RoomResponseDTO;
import br.com.hotel.hotel_reservations.exception.RoomConflictException;
import br.com.hotel.hotel_reservations.exception.RoomNotFoundException;
import br.com.hotel.hotel_reservations.mapper.RoomMapper;
import br.com.hotel.hotel_reservations.model.Room;
import br.com.hotel.hotel_reservations.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
	private final RoomMapper mapper;
	private final RoomRepository repository;
	
	@Transactional
	public RoomResponseDTO save(RoomRequestDTO requestDTO) {	
		Optional<Room> foundRoom = repository.findByRoomNumber(requestDTO.getRoomNumber());
		
		if(foundRoom.isPresent()) {
			throw new RoomConflictException(requestDTO.getRoomNumber());
		}
 		
		Room room = mapper.toEntity(requestDTO);
		repository.save(room);
		
		return mapper.toResponseDTO(room);
	}
	
	public RoomResponseDTO findById(Long id) {
		Room room = repository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
		return mapper.toResponseDTO(room);
	}
	
	/*The receptionist searches a room by number.*/
	public RoomResponseDTO findByRoomNumber(Integer roomNumber) {
		Room room = repository.findByRoomNumber(roomNumber).
		orElseThrow(() -> new RoomNotFoundException(roomNumber));
		
		return mapper.toResponseDTO(room);
	}
	
	public List<RoomResponseDTO> listAll(){
		List<Room> rooms = repository.findAll();
		return mapper.toResponseList(rooms);
	}
	
	@Transactional
	public RoomResponseDTO updateRoom(Long id, RoomRequestDTO request) {
		Optional<Room> foundRoom = repository.findByRoomNumber(request.getRoomNumber());
		Room room = repository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
		
		if (foundRoom.isPresent() && !foundRoom.get().getId().equals(room.getId())) {
			throw new RoomConflictException(request.getRoomNumber());
		}
		
		mapper.updateRoom(request, room);
		repository.save(room);
		
		return mapper.toResponseDTO(room);
	}
	
	@Transactional
	public void deleteById(Long id) {
		Room room = repository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
		repository.delete(room);
	}
	
}



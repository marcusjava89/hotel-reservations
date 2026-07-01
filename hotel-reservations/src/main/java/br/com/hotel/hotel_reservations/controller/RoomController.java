package br.com.hotel.hotel_reservations.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.hotel.hotel_reservations.dto.RoomRequestDTO;
import br.com.hotel.hotel_reservations.dto.RoomResponseDTO;
import br.com.hotel.hotel_reservations.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
	private final RoomService service;
	
	@PostMapping
	public ResponseEntity<RoomResponseDTO> save(@Valid @RequestBody RoomRequestDTO request){
		RoomResponseDTO response = service.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RoomResponseDTO> findById(@PathVariable Long id){
		RoomResponseDTO response = service.findById(id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/number")
	public ResponseEntity<RoomResponseDTO> findByRoomNumber(@RequestParam Integer roomNumber){
		RoomResponseDTO response = service.findByRoomNumber(roomNumber);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping
	public ResponseEntity<List<RoomResponseDTO>> listAll(){
		List<RoomResponseDTO> rooms = service.listAll();
		return ResponseEntity.ok(rooms);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<RoomResponseDTO> updateRoom(@PathVariable Long id,
			@Valid @RequestBody RoomRequestDTO request){
		RoomResponseDTO updatedRoom = service.updateRoom(id, request);
		return ResponseEntity.ok(updatedRoom);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}


package br.com.hotel.hotel_reservations.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.hotel.hotel_reservations.dto.ReservationRequestDTO;
import br.com.hotel.hotel_reservations.dto.ReservationResponseDTO;
import br.com.hotel.hotel_reservations.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/*We cannot have a method that deletes a reservation. Reservation is a permanent record.*/

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
	private final ReservationService service;
	
	@PostMapping
	public ResponseEntity<ReservationResponseDTO> 
	openReservation(@Valid @RequestBody ReservationRequestDTO request){
		ReservationResponseDTO response = service.openReservation(request);	
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping("/{id}/close")
	public ResponseEntity<ReservationResponseDTO> closeReservation(@PathVariable Long id){
		ReservationResponseDTO response = service.closeReservation(id);	
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PutMapping("/{id}/cancel")
	public ResponseEntity<ReservationResponseDTO> cancelReservation(@PathVariable Long id){
		ReservationResponseDTO response = service.cancelReservation(id);	
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PutMapping("/{id}/absence")
	public ResponseEntity<ReservationResponseDTO> markAbsenceReservation(@PathVariable Long id){
		ReservationResponseDTO response = service.markAbsenceReservation(id);	
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/period")
	public ResponseEntity<List<ReservationResponseDTO>> 
	findReservationsByPeriod(@RequestParam LocalDateTime startDate,@RequestParam LocalDateTime endDate){
		List<ReservationResponseDTO> responseList = service.findReservationsByPeriod(startDate, endDate);
		
		return ResponseEntity.ok(responseList);
	}
	
	@GetMapping("/roomlist")
	public ResponseEntity<List<Integer>> findAllOccupiedRooms(){
		List<Integer> roomList = service.findAllOccupiedRooms();
		return ResponseEntity.ok(roomList);
	}
	
	@GetMapping("/occupied")
	public ResponseEntity<String> checkRoomOccupation(@RequestParam Integer roomNumber){
		String message = service.checkRoomOccupation(roomNumber);
		return ResponseEntity.ok(message);
	}
}
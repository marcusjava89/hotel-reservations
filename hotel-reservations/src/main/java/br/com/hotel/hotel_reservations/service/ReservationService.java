package br.com.hotel.hotel_reservations.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.hotel.hotel_reservations.dto.ReservationRequestDTO;
import br.com.hotel.hotel_reservations.dto.ReservationResponseDTO;
import br.com.hotel.hotel_reservations.enums.ReservationStatus;
import br.com.hotel.hotel_reservations.exception.CustomerNotFoundException;
import br.com.hotel.hotel_reservations.exception.InvalidReservationDateException;
import br.com.hotel.hotel_reservations.exception.ReservationConflictException;
import br.com.hotel.hotel_reservations.exception.ReservationNotFoundException;
import br.com.hotel.hotel_reservations.exception.ReservationStatusInvalidException;
import br.com.hotel.hotel_reservations.exception.RoomNotFoundException;
import br.com.hotel.hotel_reservations.mapper.ReservationMapper;
import br.com.hotel.hotel_reservations.model.Customer;
import br.com.hotel.hotel_reservations.model.Reservation;
import br.com.hotel.hotel_reservations.model.Room;
import br.com.hotel.hotel_reservations.repository.CustomerRepository;
import br.com.hotel.hotel_reservations.repository.ReservationRepository;
import br.com.hotel.hotel_reservations.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
	private final ReservationMapper mapper;
	private final ReservationRepository reservationRepository;
	private final CustomerRepository customerRepository;
	private final RoomRepository roomRepository;
	
	@Transactional
	@CacheEvict(value = {"reservationPeriod", "occupiedRooms", "checkRoom"} ,allEntries = true)
	public ReservationResponseDTO openReservation(ReservationRequestDTO request) {
		
			if(!request.getCheckin().isBefore(request.getCheckout())) {
				throw new InvalidReservationDateException(request.getCheckin(), request.getCheckout());
			}	
		
			Customer customer = customerRepository.findByEmail(request.getCustomerEmail())
					.orElseThrow(() -> new CustomerNotFoundException(request.getCustomerEmail()));
			
			Room room = roomRepository.findByRoomNumber(request.getRoomNumber()).
					orElseThrow(() -> new RoomNotFoundException(request.getRoomNumber()));
			
			boolean dateConflict = reservationRepository.existsReservationConflict(
					room, request.getCheckin(), request.getCheckout());
			
			if(dateConflict) {
				throw new ReservationConflictException(room.getRoomNumber());
			}
			
			Reservation reservation = mapper.toEntity(request);
			
			reservation.setStatus(ReservationStatus.SCHEDULED);
			reservation.setCustomer(customer);
			reservation.setRoom(room);
			
			reservationRepository.save(reservation);
			
			log.info("Reservation created. Id: {}.", reservation.getId());
			
			return mapper.toResponse(reservation);
	}
	
	@Transactional
	@CacheEvict(value = {"reservationPeriod", "occupiedRooms", "checkRoom"} ,allEntries = true)
	public ReservationResponseDTO closeReservation(Long id){
		Reservation reservation = reservationRepository.findById(id).orElseThrow(
				() -> new ReservationNotFoundException(id));
		
		if(reservation.getStatus().equals(ReservationStatus.IN_USE) || 
				reservation.getStatus().equals(ReservationStatus.SCHEDULED)) {
			reservation.setStatus(ReservationStatus.FINISHED);
		}else {
			throw new ReservationStatusInvalidException(reservation.getStatus());
		}
		
		reservationRepository.save(reservation);
		
		log.info("Reservation closed. Id: {}.", reservation.getId());
		
		return mapper.toResponse(reservation);
	}
	
	@Transactional
	@CacheEvict(value = {"reservationPeriod", "occupiedRooms", "checkRoom"} ,allEntries = true)
	public ReservationResponseDTO cancelReservation(Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow(
				() -> new ReservationNotFoundException(id));
		
		if(reservation.getStatus() == ReservationStatus.SCHEDULED) {
			reservation.setStatus(ReservationStatus.CANCELED);
		}else {
			throw new ReservationStatusInvalidException(reservation.getStatus());
		}
		
		reservationRepository.save(reservation);
		
		log.info("Reservation canceled. Id: {}.", reservation.getId());
		
		return mapper.toResponse(reservation);
	}
	
	@Transactional
	@CacheEvict(value = {"reservationPeriod", "occupiedRooms", "checkRoom"} ,allEntries = true)
	public ReservationResponseDTO markAbsenceReservation(Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow(
				() -> new ReservationNotFoundException(id));
		
		if(reservation.getStatus() == ReservationStatus.SCHEDULED) {
			reservation.setStatus(ReservationStatus.ABSENCE);
		}else {
			throw new ReservationStatusInvalidException(reservation.getStatus());
		}
		
		reservationRepository.save(reservation);
		log.info("Reservation marked absence. Id: {}.", reservation.getId());
		return mapper.toResponse(reservation);
	}
	
	@Cacheable("reservationPeriod")
	public List<ReservationResponseDTO> findReservationsByPeriod(LocalDateTime startDate, LocalDateTime endDate){
		log.info("Searching reservations between {} and {}.", startDate, endDate);
		
		List<Reservation> reservationList = reservationRepository.findReservationsByPeriod(startDate, endDate);
		return mapper.toResponseList(reservationList);
	}
	
	@Cacheable("occupiedRooms")
	public List<Integer> findAllOccupiedRooms(){
		log.info("Searching occupied rooms.");
		
		List<Integer> occupiedRoomList = reservationRepository.findAllOccupiedRooms();
		return occupiedRoomList;
 	}
	
	@Cacheable("checkRoom")
	public String checkRoomOccupation(Integer roomNumber) {
		log.info("Checking occupation of room {}.", roomNumber);
		
		roomRepository.findByRoomNumber(roomNumber).orElseThrow(() -> new RoomNotFoundException(roomNumber));
		
		boolean occupied = reservationRepository.isRoomOccupied(roomNumber);
		
		if(occupied) {
			return "The room "+roomNumber+" is occupied.";
		}else {
			return "The room "+roomNumber+" is not occupied.";
		}
	}
	
}
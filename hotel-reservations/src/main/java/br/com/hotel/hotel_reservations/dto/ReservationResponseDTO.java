package br.com.hotel.hotel_reservations.dto;

import java.time.LocalDateTime;

import br.com.hotel.hotel_reservations.enums.ReservationStatus;

public record ReservationResponseDTO(Long id, String customerName, Integer roomNumber, LocalDateTime checkin,
		LocalDateTime checkout, ReservationStatus status, LocalDateTime createdAt) {

}

package br.com.hotel.hotel_reservations.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReservationRequestDTO {
	
	@NotBlank(message = "Email cannot be empty.")
	private String customerEmail; 
	
	@NotNull(message = "Room number cannot be empty.")
	private Integer roomNumber;

	@NotNull(message = "Check-in cannot be empty.")
	private LocalDateTime checkin;
	
	@NotNull(message = "Check-out cannot be empty.")
	private LocalDateTime checkout;
	
}
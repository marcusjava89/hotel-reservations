package br.com.hotel.hotel_reservations.exception;

import java.time.LocalDateTime;

public class InvalidReservationDateException extends RuntimeException{
	public InvalidReservationDateException(LocalDateTime checkIn, LocalDateTime checkOut) {
		super("The check-in is after or equals check-out.");
	}
}

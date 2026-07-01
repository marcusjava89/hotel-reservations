package br.com.hotel.hotel_reservations.exception;

import br.com.hotel.hotel_reservations.enums.ReservationStatus;

public class ReservationStatusInvalidException extends RuntimeException{
	public ReservationStatusInvalidException(ReservationStatus status) {
		super("Reservation status, "+status+", is invalid for this operation.");
	}
}

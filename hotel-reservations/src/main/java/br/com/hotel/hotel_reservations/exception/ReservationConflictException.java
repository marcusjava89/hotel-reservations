package br.com.hotel.hotel_reservations.exception;

public class ReservationConflictException extends RuntimeException{
	public ReservationConflictException(Integer roomNumber) {
		super("Room "+roomNumber+" already reserved for required period.");
	}
}

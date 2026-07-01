package br.com.hotel.hotel_reservations.exception;

public class ReservationNotFoundException extends RuntimeException{
	public ReservationNotFoundException(Long id) {
		super("The reservation "+id+" was not found.");
	}
}

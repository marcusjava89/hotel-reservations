package br.com.hotel.hotel_reservations.exception;

public class CustomerConflictException extends RuntimeException{
	public CustomerConflictException(String email) {
		super("A customer with email = "+email+" already exist.");
	}
}

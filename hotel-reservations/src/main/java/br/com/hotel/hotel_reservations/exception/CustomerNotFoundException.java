package br.com.hotel.hotel_reservations.exception;

public class CustomerNotFoundException extends RuntimeException{
	
	public CustomerNotFoundException(Long id){
		super("Customer with id = "+id+" was not found.");
	}
	
	public CustomerNotFoundException(String email){
		super("Customer with email = "+email+" was not found.");
	}
}

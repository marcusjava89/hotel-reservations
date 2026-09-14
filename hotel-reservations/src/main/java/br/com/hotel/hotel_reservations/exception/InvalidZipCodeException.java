package br.com.hotel.hotel_reservations.exception;

public class InvalidZipCodeException extends RuntimeException{
	
	public InvalidZipCodeException(String zipCode) {
		super("The zipcode "+zipCode+" was not found.");
	}
}

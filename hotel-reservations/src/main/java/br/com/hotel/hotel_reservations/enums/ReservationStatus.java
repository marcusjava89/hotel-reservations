package br.com.hotel.hotel_reservations.enums;

public enum ReservationStatus {
	/*The room was selected for a specified period (check-in).*/
	SCHEDULED, 
	
	/*The room is occupied in the moment.*/
	IN_USE, 
	
	/*The customer did not show up. This is a final situation.*/
	ABSENCE, 
	
	/*The customer stayed in the room and left the hotel. Successful reservation.*/
	FINISHED, 
	
	/*Reservation canceled before check-in.*/
	CANCELED
}

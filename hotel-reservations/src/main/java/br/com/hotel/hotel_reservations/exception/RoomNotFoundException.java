package br.com.hotel.hotel_reservations.exception;

public class RoomNotFoundException extends RuntimeException{
	
	public RoomNotFoundException(Long id) {
		super("Room not found, id: "+id);
	}
	
	public RoomNotFoundException(Integer roomNumber) {
		super("Room not found, room number: "+roomNumber);
	}
	
}

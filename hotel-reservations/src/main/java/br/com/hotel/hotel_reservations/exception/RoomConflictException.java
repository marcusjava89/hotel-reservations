package br.com.hotel.hotel_reservations.exception;

public class RoomConflictException extends RuntimeException{
	public RoomConflictException(Integer roomNumber) {
		super("A room with this number already exists. Room number: "+roomNumber);
	}
}

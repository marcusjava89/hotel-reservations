package br.com.hotel.hotel_reservations.dto;

public record AddressResponseDTO(Long id, String zipCode,  String street, String addressDetails,
		String neighborhood, String state) {
	
}
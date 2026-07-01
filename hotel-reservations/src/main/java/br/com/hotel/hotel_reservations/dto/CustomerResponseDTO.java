package br.com.hotel.hotel_reservations.dto;

import java.time.LocalDateTime;

public record CustomerResponseDTO(Long id, String name, String email, String phone, LocalDateTime createdAt,
		AddressResponseDTO address) {
	
}

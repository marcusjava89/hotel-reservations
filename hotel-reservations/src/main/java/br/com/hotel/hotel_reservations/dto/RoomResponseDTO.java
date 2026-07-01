package br.com.hotel.hotel_reservations.dto;

import java.math.BigDecimal;

import br.com.hotel.hotel_reservations.enums.RoomType;

public record RoomResponseDTO(Long id, Integer roomNumber, RoomType type, BigDecimal price) {

}

package br.com.hotel.hotel_reservations.dto;

import java.math.BigDecimal;

import br.com.hotel.hotel_reservations.enums.RoomType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomRequestDTO {
	
	@NotNull(message = "Room number is required.")
	@Positive(message = "Room number must be bigger than zero.")
	private Integer roomNumber;
	
	@NotNull(message = "Room type is required.")
	private RoomType type;
	
	@NotNull(message = "Room price is required.")
	@DecimalMin(value = "0.0", inclusive = false)
	private BigDecimal price;
}

package br.com.hotel.hotel_reservations.model;

import java.math.BigDecimal;

import br.com.hotel.hotel_reservations.enums.RoomType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rooms")
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Room {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	@Setter(AccessLevel.NONE)
	private Long id;
	
	@Column(name = "room_number", nullable = false, unique = true)
	private Integer roomNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private RoomType type;
	
	@Column(name = "price", nullable = false)
	private BigDecimal price;
}

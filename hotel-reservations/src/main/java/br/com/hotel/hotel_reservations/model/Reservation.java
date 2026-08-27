package br.com.hotel.hotel_reservations.model;

import java.time.LocalDateTime;

import br.com.hotel.hotel_reservations.enums.ReservationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservations")
@Getter @Setter
@EqualsAndHashCode(of = "id")
@SequenceGenerator(name = "seq_reservation", sequenceName = "seq_reservation", initialValue = 1, allocationSize = 1)
public class Reservation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reservation")
	@Setter(AccessLevel.NONE)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", nullable = false)
	private Room room;

	@Column(name = "checkin", nullable = false)
	private LocalDateTime checkin;
	
	@Column(name = "checkout", nullable = false)
	private LocalDateTime checkout;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ReservationStatus status;
	
	/*We need to know when the reservation was created for future records.*/
	@Column(name = "created_at", nullable =  false)
	@Setter(AccessLevel.NONE)
	private LocalDateTime createdAt;
	
	@PrePersist
	void prePersist() {
		createdAt = LocalDateTime.now();
	}
	
}
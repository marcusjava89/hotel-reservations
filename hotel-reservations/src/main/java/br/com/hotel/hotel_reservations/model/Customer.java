package br.com.hotel.hotel_reservations.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "customers")
@Getter @Setter
@EqualsAndHashCode(of = "id")
@SequenceGenerator(name = "seq_customer", sequenceName = "seq_customer", initialValue = 1, allocationSize = 1)
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_customer")
	@Column(name = "id_customer", nullable = false)
	@Setter(AccessLevel.NONE)
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "phone", nullable = false)
	private String phone;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	@Setter(AccessLevel.NONE)
	private LocalDateTime createdAt;

	@PrePersist //Executed before persist object
	public void prePersist() {
		createdAt = LocalDateTime.now();
	}
	
	@JoinColumn(name = "id_address", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	public Address address; //ForeignKey
}

package br.com.hotel.hotel_reservations.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/*fazer responseDTO, repository, mapper*/

@Entity
@Table(name = "addresses")
@Getter @Setter
@EqualsAndHashCode(of = "id")
@SequenceGenerator(name = "seq_address", sequenceName = "seq_address", initialValue = 1, allocationSize = 1)
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_address")
	@Column(name = "id_address", nullable = false)
	@Setter(AccessLevel.NONE)
	public Long id;
	
	@Column(name = "zip_code", nullable = false)
	public String zipCode;
	
	@Column(name = "street", nullable = false)
	public String street;
	
	@Column(name = "address_details", nullable = false)
	public String addressDetails;
	
	@Column(name = "neighborhood", nullable = false)
	public String neighborhood;
	
	@Column(name = "state", nullable = false)
	public String state;
	
}
package br.com.hotel.hotel_reservations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddressRequestDTO {
	
	@NotBlank(message = "Zip code cannot be empty")
	@Pattern(regexp = "\\d{8}", message = "Zipcode must have 8 digits.")
	public String zipCode;
	
	@NotBlank(message = "Street name cannot be empty.")
	public String street;
	
	@NotBlank(message = "Address details cannot be empty.")
	public String addressDetails;
	
	@NotBlank(message = "The neighborhood name cannot be empty.")
	public String neighborhood;
	
	@NotBlank(message = "The State name cannot be empty.")
	public String state;
}

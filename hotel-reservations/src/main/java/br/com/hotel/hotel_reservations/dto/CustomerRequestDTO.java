package br.com.hotel.hotel_reservations.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerRequestDTO {
	
	@NotBlank(message = "Name cannot be empty")
	@Size(min = 3, max = 60)
	private String name;
	
	@NotBlank(message = "E-mail address cannot be empty.")
	@Email(message = "Invalid e-mail address format.")
	private String email;
	
	@NotBlank(message = "We need a phone to contact the customer.")
	private String phone;
	
	@NotBlank(message = "Zipcode is required.")
	private String zipcode;
}
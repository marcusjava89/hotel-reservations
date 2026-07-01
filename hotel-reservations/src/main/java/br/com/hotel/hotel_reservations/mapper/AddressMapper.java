package br.com.hotel.hotel_reservations.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.hotel.hotel_reservations.dto.AddressRequestDTO;
import br.com.hotel.hotel_reservations.dto.AddressResponseDTO;
import br.com.hotel.hotel_reservations.model.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

	@Mapping(target = "id", ignore = true)
	public Address toEntity(AddressRequestDTO addressRequestDTO);
	
	public AddressResponseDTO toResponse(Address address);
}

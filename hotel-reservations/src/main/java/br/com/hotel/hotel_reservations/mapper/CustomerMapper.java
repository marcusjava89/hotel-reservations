package br.com.hotel.hotel_reservations.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import br.com.hotel.hotel_reservations.dto.CustomerRequestDTO;
import br.com.hotel.hotel_reservations.dto.CustomerResponseDTO;
import br.com.hotel.hotel_reservations.model.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

	@Mapping(target = "id", ignore = true)
	public Customer toEntity(CustomerRequestDTO request);
	
	public CustomerResponseDTO toResponse(Customer customer);
	
	public List<CustomerResponseDTO> toResponseList(List<Customer> list); 
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	public void updateCustomer(CustomerRequestDTO request, @MappingTarget Customer customer);
}

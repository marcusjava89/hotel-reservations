package br.com.hotel.hotel_reservations.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import br.com.hotel.hotel_reservations.dto.CustomerResponseDTO;
import br.com.hotel.hotel_reservations.dto.ReservationRequestDTO;
import br.com.hotel.hotel_reservations.dto.ReservationResponseDTO;
import br.com.hotel.hotel_reservations.model.Customer;
import br.com.hotel.hotel_reservations.model.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
	
	@Mapping(target = "id", ignore = true)
	public Reservation toEntity(ReservationRequestDTO request);
	
	@Mapping(target = "customerName", source = "customer.name")
	@Mapping(target = "roomNumber", source = "room.roomNumber")
	public ReservationResponseDTO toResponse(Reservation reservation);
	
	public List<ReservationResponseDTO> toResponseList(List<Reservation> list); 

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	public void updateReservation(ReservationRequestDTO request, @MappingTarget Reservation reservation);
}

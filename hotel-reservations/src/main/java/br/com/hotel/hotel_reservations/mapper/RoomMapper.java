package br.com.hotel.hotel_reservations.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import br.com.hotel.hotel_reservations.dto.RoomRequestDTO;
import br.com.hotel.hotel_reservations.dto.RoomResponseDTO;
import br.com.hotel.hotel_reservations.model.Room;

@Mapper(componentModel = "spring")
public interface RoomMapper {
	
	@Mapping( target = "id", ignore = true)
	public Room toEntity(RoomRequestDTO request);
	
	public RoomResponseDTO toResponseDTO(Room room);
	
	public List<RoomResponseDTO> toResponseList(List<Room> roomList);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	public void updateRoom(RoomRequestDTO request, @MappingTarget Room room);
}

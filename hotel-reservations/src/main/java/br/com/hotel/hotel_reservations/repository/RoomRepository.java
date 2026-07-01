package br.com.hotel.hotel_reservations.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.hotel.hotel_reservations.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{
	public Optional<Room> findByRoomNumber(Integer roomNumber);
}

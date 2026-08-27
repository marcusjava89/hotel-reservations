package br.com.hotel.hotel_reservations.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.hotel.hotel_reservations.enums.ReservationStatus;
import br.com.hotel.hotel_reservations.model.Reservation;
import br.com.hotel.hotel_reservations.model.Room;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query("""
			SELECT COUNT(r) > 0
			FROM Reservation r
			WHERE r.room = :room
			AND r.status IN ('IN_USE', 'SCHEDULED')
			AND r.checkin < :endDate
			AND r.checkout > :startDate
			""")
	public boolean existsReservationConflict(@Param("room") Room room, @Param("startDate") LocalDateTime startDate, 
			@Param("endDate") LocalDateTime endDate);

	@Query("""
			SELECT r
			FROM Reservation r
			WHERE r.checkin < :endDate
			AND r.checkout > :startDate
			""")
	public List<Reservation> findReservationsByPeriod(@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);
	
	@Query("""
		       SELECT r.room.roomNumber
		       FROM Reservation r
		       WHERE r.status = 'IN_USE'
		       """)
	public List<Integer> findAllOccupiedRooms();
	
	@Query("""
		       SELECT COUNT(r) > 0
		       FROM Reservation r
		       WHERE r.status IN ('IN_USE', 'SCHEDULED')
		       AND r.room.roomNumber = :roomNumber
		       """)
	public boolean isRoomOccupied(Integer roomNumber);

}
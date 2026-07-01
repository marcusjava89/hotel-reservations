package br.com.hotel.hotel_reservations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hotel.hotel_reservations.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}

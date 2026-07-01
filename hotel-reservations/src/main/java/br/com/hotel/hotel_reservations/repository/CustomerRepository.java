package br.com.hotel.hotel_reservations.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.hotel.hotel_reservations.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	public Optional<Customer> findByEmail(String email);
	
}

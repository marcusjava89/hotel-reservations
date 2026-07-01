package br.com.hotel.hotel_reservations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.hotel.hotel_reservations.dto.CustomerRequestDTO;
import br.com.hotel.hotel_reservations.dto.CustomerResponseDTO;
import br.com.hotel.hotel_reservations.exception.CustomerConflictException;
import br.com.hotel.hotel_reservations.exception.CustomerNotFoundException;
import br.com.hotel.hotel_reservations.mapper.CustomerMapper;
import br.com.hotel.hotel_reservations.model.Customer;
import br.com.hotel.hotel_reservations.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository repository;
	private final CustomerMapper mapper;

	@Transactional
	public CustomerResponseDTO save(CustomerRequestDTO request) {
		Optional<Customer> foundCustomer = repository.findByEmail(request.getEmail());

		if (foundCustomer.isPresent()) {
			throw new CustomerConflictException(request.getEmail());
		}

		Customer customer = repository.save(mapper.toEntity(request));
		return mapper.toResponse(customer);
	}

	public CustomerResponseDTO findById(Long id) {
		Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
		return mapper.toResponse(customer);
	}

	/*The receptionist searches a customer by e-mail address.*/
	public CustomerResponseDTO findByEmail(String email) {
		Customer customer = repository.findByEmail(email).orElseThrow(() -> new CustomerNotFoundException(email));
		return mapper.toResponse(customer);
	}

	@Transactional
	public void delete(Long id) {
		Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
		repository.delete(customer);
	}

	public List<CustomerResponseDTO> listAll() {
		List<Customer> listCustomer = repository.findAll();
		return mapper.toResponseList(listCustomer);
	}
	
	@Transactional
	public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request) {	
		Optional<Customer> found = repository.findByEmail(request.getEmail());
		Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));

		if (found.isPresent() && !found.get().getId().equals(customer.getId())) {
			throw new CustomerConflictException(request.getEmail());
		}
		
		mapper.updateCustomer(request, customer);
		repository.save(customer);
		return mapper.toResponse(customer);
	}
	
}
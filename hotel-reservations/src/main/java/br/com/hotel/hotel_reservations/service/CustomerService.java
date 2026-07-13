package br.com.hotel.hotel_reservations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
	private final CustomerRepository repository;
	private final CustomerMapper mapper;

	@Transactional
	public CustomerResponseDTO save(CustomerRequestDTO request) {
		Optional<Customer> foundCustomer = repository.findByEmail(request.getEmail());

		if (foundCustomer.isPresent()) {
			log.warn("Attempt to create a customer with an existent email: {}", request.getEmail());
			
			throw new CustomerConflictException(request.getEmail());
		}

		Customer customer = repository.save(mapper.toEntity(request));
		
		log.info("New customer added, with email {}.", customer.getEmail());
		
		return mapper.toResponse(customer);
	}

	@Cacheable("customers")
	public CustomerResponseDTO findById(Long id) {
		
		log.info("Searching customer {} in database.", id);
		
		Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
		return mapper.toResponse(customer);
	}

	/*The concierge searches a customer by e-mail address.*/
	@Cacheable("customersEmail")
	public CustomerResponseDTO findByEmail(String email) {
		
		log.info("Searching customer with email: {}.", email);
		
		Customer customer = repository.findByEmail(email).orElseThrow(() -> new CustomerNotFoundException(email));
		return mapper.toResponse(customer);
	}

	/*Keep from here.*/
	
	@Transactional
	@CacheEvict(value = {"customers", "customersEmail"}, allEntries = true)
	public void delete(Long id) {
		Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
		repository.delete(customer);
	}

	public List<CustomerResponseDTO> listAll() {
		List<Customer> listCustomer = repository.findAll();
		return mapper.toResponseList(listCustomer);
	}
	
	@Transactional
	@CacheEvict(value = {"customers", "customersEmail"}, allEntries = true)
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
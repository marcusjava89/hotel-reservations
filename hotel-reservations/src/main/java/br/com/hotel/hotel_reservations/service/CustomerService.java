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
import br.com.hotel.hotel_reservations.exception.InvalidZipCodeException;
import br.com.hotel.hotel_reservations.integration.viacep.ViaCepClient;
import br.com.hotel.hotel_reservations.integration.viacep.ViaCepResponse;
import br.com.hotel.hotel_reservations.mapper.CustomerMapper;
import br.com.hotel.hotel_reservations.model.Address;
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
	private final ViaCepClient viaCepClient;

	@Transactional
	public CustomerResponseDTO save(CustomerRequestDTO request) {
		Optional<Customer> foundCustomer = repository.findByEmail(request.getEmail());

		if (foundCustomer.isPresent()) {
			throw new CustomerConflictException(request.getEmail());
		}
		
		ViaCepResponse viaCepResponse = viaCepClient.findByZipCode(request.getZipcode());
		
		if(Boolean.TRUE.equals(viaCepResponse.erro())) {
			throw new InvalidZipCodeException(request.getZipcode());
		}
		
		Address address = new Address();
		
		address.setAddressDetails(viaCepResponse.complemento());
		address.setNeighborhood(viaCepResponse.bairro());
		address.setState(viaCepResponse.uf());
		address.setStreet(viaCepResponse.logradouro());
		address.setZipCode(viaCepResponse.cep());
		
		Customer customer = mapper.toEntity(request);
		customer.setAddress(address);
		
		repository.save(customer);
		
		log.info("Customer created and saved in database. Email: {}", customer.getEmail());
		
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
	
	@Transactional
	@CacheEvict(value = {"customers", "customersEmail"}, allEntries = true)
	public void delete(Long id) {
		Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
		repository.delete(customer);
		
		log.info("Customer deleted. Id: {}.", id);	
	}

	public List<CustomerResponseDTO> listAll() {
		List<Customer> listCustomer = repository.findAll();
		log.info("All customers were listed.");
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

		log.info("Customer updated. Id: {}. E-mail: {}", customer.getId(), customer.getEmail());
		
		return mapper.toResponse(customer);
		
	}
}
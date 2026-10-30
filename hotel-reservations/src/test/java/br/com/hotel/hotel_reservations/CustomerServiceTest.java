package br.com.hotel.hotel_reservations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.hotel.hotel_reservations.dto.CustomerRequestDTO;
import br.com.hotel.hotel_reservations.dto.CustomerResponseDTO;
import br.com.hotel.hotel_reservations.exception.CustomerConflictException;
import br.com.hotel.hotel_reservations.exception.InvalidZipCodeException;
import br.com.hotel.hotel_reservations.integration.viacep.ViaCepClient;
import br.com.hotel.hotel_reservations.integration.viacep.ViaCepResponse;
import br.com.hotel.hotel_reservations.mapper.CustomerMapper;
import br.com.hotel.hotel_reservations.model.Customer;
import br.com.hotel.hotel_reservations.repository.CustomerRepository;
import br.com.hotel.hotel_reservations.service.CustomerService;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
	
	@InjectMocks
	private CustomerService service;
	
	@Mock
	private CustomerRepository repository;
	
	@Mock
	private ViaCepClient viaCepClient;
	
	@Mock
	private CustomerMapper mapper;
	
	private CustomerRequestDTO request;
	
	@BeforeEach
	public void setup() {
		request = new CustomerRequestDTO();
		request.setName("Marcus Martins");
		request.setEmail("marcus@gmail.com");
		request.setPhone("21333658745");
		request.setZipcode("20040020");
	}
	
	@Test
	@DisplayName("Method save customer successfully.")
	public void saveCustomer_Successfully() {
		
		when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
		
		ViaCepResponse viaCepResponse = new ViaCepResponse("20040-020","Rua Primeiro de Março","","Centro","RJ",
				false);
		
		when(viaCepClient.findByZipCode(request.getZipcode())).thenReturn(viaCepResponse);
		
		Customer customer = new Customer();
		customer.setName(request.getName());
		customer.setEmail(request.getEmail());
		customer.setPhone(request.getPhone());
		
		when(mapper.toEntity(request)).thenReturn(customer);
		
		CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(1L, request.getName(), 
				request.getEmail(),request.getPhone(), LocalDateTime.now(), null);
		
		when(mapper.toResponse(customer)).thenReturn(customerResponseDTO);

		CustomerResponseDTO result = service.save(request);
		
		assertNotNull(result);
		assertEquals(request.getName(), result.name());
		assertEquals(request.getEmail(), result.email());
		assertEquals(request.getPhone(), result.phone());
		
		verify(mapper).toEntity(request);
		verify(mapper).toResponse(customer);
		
		ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
		
		verify(repository).save(captor.capture());
		
		Customer savedCustomer = captor.getValue();
		
		assertNotNull(savedCustomer.getAddress());
		assertEquals("20040-020", savedCustomer.getAddress().getZipCode());
		assertEquals("Rua Primeiro de Março", savedCustomer.getAddress().getStreet());
	}
	
	@Test
	@DisplayName("We try to save customer and the email alredy exists.")
	public void saveEmailAlredyExists() {
		Customer customer = new Customer();
		customer.setName("Idris Elba");
		customer.setEmail("marcus@gmail.com");
		
		when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(customer));
		
		CustomerConflictException ex = assertThrows(CustomerConflictException.class, () -> service.save(request));
		
		assertEquals("A customer with email = marcus@gmail.com already exist.", ex.getMessage());
		
		verify(repository).findByEmail(request.getEmail());
		verify(repository, never()).save(any(Customer.class));
	}
	
	@Test
	@DisplayName("We try to save a customer and the zipcode is invalid.")
	public void SaveInvalidZipcode() {
		request.setZipcode("2345");
		
		ViaCepResponse viaCepResponse = new ViaCepResponse(null, null, null, null, null, true);
		
		when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
		
		when(viaCepClient.findByZipCode(request.getZipcode())).thenReturn(viaCepResponse);
		
		InvalidZipCodeException ex = assertThrows(InvalidZipCodeException.class,() -> service.save(request));
		
		assertEquals("The zipcode 2345 was not found.", ex.getMessage());
		
		verify(repository).findByEmail(request.getEmail());
		verify(viaCepClient).findByZipCode(request.getZipcode());
		verify(repository, never()).save(any(Customer.class));

	}
	
}



















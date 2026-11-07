package br.com.hotel.hotel_reservations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
import br.com.hotel.hotel_reservations.exception.CustomerNotFoundException;
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
	
	private Customer customer1;
	
	public void setupCustomer() {
	customer1 = new Customer();
	customer1.setName("Idris Elba");
	customer1.setEmail("idris@gmail.com");
	customer1.setPhone("21789653625");
	customer1.setAddress(null);
	}
	
	public void setupRequest() {
		request = new CustomerRequestDTO();
		request.setName("Marcus Martins");
		request.setEmail("marcus@gmail.com");
		request.setPhone("21333658745");
		request.setZipcode("20040020");
	}
	
	@Test
	@DisplayName("Method save customer successfully.")
	public void saveCustomerSuccessfully() {
		setupRequest();
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
		assertEquals(customerResponseDTO.id(), result.id());
		assertEquals(customerResponseDTO.name(), result.name());
		assertEquals(customerResponseDTO.email(), result.email());
		assertEquals(customerResponseDTO.phone(), result.phone());
		assertNull(result.address());
		
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
		setupCustomer();
		setupRequest();
		
		customer1.setEmail("marcus@gmail.com");
		
		when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(customer1));
		
		CustomerConflictException ex = assertThrows(CustomerConflictException.class, () -> service.save(request));
		
		assertEquals("A customer with email = marcus@gmail.com already exist.", ex.getMessage());
		
		verify(repository).findByEmail(request.getEmail());
		verify(repository, never()).save(any(Customer.class));
	}
	
	@Test
	@DisplayName("We try to save a customer and the zipcode is invalid.")
	public void saveInvalidZipcode() {
		setupRequest();
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
	
	@Test
	@DisplayName("Tries to find customer by id and complte the task.")
	public void findByIdSuccessfully() {
		setupCustomer();
		
		when(repository.findById(1L)).thenReturn(Optional.of(customer1));
		
		CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(1L, customer1.getName(), 
				customer1.getEmail(), customer1.getPhone(), customer1.getCreatedAt(), null);
		
		when(mapper.toResponse(customer1)).thenReturn(customerResponseDTO);
		
		CustomerResponseDTO result = service.findById(1L);
		
		assertNotNull(result);
		assertEquals(customerResponseDTO.id(), result.id());
		assertEquals("Idris Elba", result.name());
		assertEquals("idris@gmail.com", result.email());
		assertEquals("21789653625", result.phone());
		assertNull(result.address());
		
		verify(repository).findById(1L);
		verify(mapper).toResponse(customer1);
	}
	
	@Test
	@DisplayName("Tries to found customer by id and customer not found.")
	public void findByIdCustomerNotFound() {
		when(repository.findById(1L)).thenReturn(Optional.empty());
		
		CustomerNotFoundException ex = assertThrows( CustomerNotFoundException.class, () -> service.findById(1L));
		
		assertEquals("Customer with id = 1 was not found.", ex.getMessage());
		
		verify(repository).findById(1L);
		verify(mapper, never()).toResponse(any(Customer.class));

	}
	
	@Test
	@DisplayName("Tries to found customer by email and succeed.")
	public void findByEmailSuccessfully() {
		setupCustomer();
		
		when(repository.findByEmail("idris@gmail.com")).thenReturn(Optional.of(customer1));
		
		CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(1L, customer1.getName(), 
				customer1.getEmail(), customer1.getPhone(), customer1.getCreatedAt(), null);
		
		when(mapper.toResponse(customer1)).thenReturn(customerResponseDTO);
		
		CustomerResponseDTO result = service.findByEmail("idris@gmail.com");
		
		assertNotNull(result);
		assertEquals(customerResponseDTO.id(), result.id());
		assertEquals("Idris Elba", result.name());
		assertEquals("idris@gmail.com", result.email());
		assertEquals("21789653625", result.phone());
		assertNull(result.address());
		
		verify(repository).findByEmail("idris@gmail.com");
		verify(mapper).toResponse(customer1);
	}
	
	@Test
	@DisplayName("Tries to found customer by email and fails.")
	public void findByEmailCustomerNotFound() {
		when(repository.findByEmail("idris@gmail.com")).thenReturn(Optional.empty());
		
		CustomerNotFoundException ex = assertThrows(CustomerNotFoundException.class, 
				() -> service.findByEmail("idris@gmail.com"));
		
		assertEquals("Customer with email = idris@gmail.com was not found.", ex.getMessage());
		
		verify(repository).findByEmail("idris@gmail.com");
		verify(mapper, never()).toResponse(any(Customer.class));
	}
	
	@Test
	@DisplayName("Deletes customer by id successfully.")
	public void deleteSuccessfully() {
		setupCustomer();
		
		when(repository.findById(1L)).thenReturn(Optional.of(customer1));
		
		service.delete(1L);
		verify(repository).findById(1L);
		verify(repository).delete(customer1);
	}
	
	@Test
	@DisplayName("Customer not found by id.")
	public void deleteCustomerNotFoundCustomer() {
		when(repository.findById(1L)).thenReturn(Optional.empty());
		
		CustomerNotFoundException ex = assertThrows(CustomerNotFoundException.class, () -> service.delete(1L));
		
		assertEquals("Customer with id = 1 was not found.", ex.getMessage());
		verify(repository).findById(1L);
		verify(repository, never()).delete(any(Customer.class));
		
	}
	
	@Test
	@DisplayName("List all customers")
	public void listAllCustomerList() {
		setupCustomer();
		
		Customer customer2 = new Customer();
		customer2.setName("Megan Good");
		customer2.setEmail("megan@gmail.com");
		customer2.setPhone("21456987123");
		customer2.setAddress(null);
		
		List<Customer> list = List.of(customer1, customer2);
		
		when(repository.findAll()).thenReturn(list);
		
		CustomerResponseDTO customerResponseDTO1 = new CustomerResponseDTO(1L, customer1.getName(), 
				customer1.getEmail(), customer1.getPhone(), customer1.getCreatedAt(), null);
		
		CustomerResponseDTO customerResponseDTO2 = new CustomerResponseDTO(2L, customer2.getName(), 
				customer2.getEmail(), customer2.getPhone(), customer2.getCreatedAt(), null);
		
		List<CustomerResponseDTO> listResponse = List.of(customerResponseDTO1, customerResponseDTO2);
		
		when(mapper.toResponseList(list)).thenReturn(listResponse);
		
		List<CustomerResponseDTO> listResult = service.listAll();
		
		assertNotNull(listResult);
		assertNull(listResult.get(0).address());
		assertEquals(listResponse.get(0).id(), listResult.get(0).id());
		assertEquals(listResponse.get(0).name(), listResult.get(0).name());
		assertEquals(listResponse.get(0).email(), listResult.get(0).email());
		assertEquals(listResponse.get(0).phone(), listResult.get(0).phone());
		assertEquals(listResponse.get(0).createdAt(), listResult.get(0).createdAt());
		
		assertNull(listResult.get(1).address());
		assertEquals(listResponse.get(1).id(), listResult.get(1).id());
		assertEquals(listResponse.get(1).name(), listResult.get(1).name());
		assertEquals(listResponse.get(1).email(), listResult.get(1).email());
		assertEquals(listResponse.get(1).phone(), listResult.get(1).phone());
		assertEquals(listResponse.get(1).createdAt(), listResult.get(1).createdAt());
		
		assertEquals(2, listResult.size());
		
		verify(repository).findAll();
		verify(mapper).toResponseList(list);
	}
	
	@Test
	@DisplayName("List all customers")
	public void listAllEmptyList() {
		List<Customer> list = List.of();
		
		when(repository.findAll()).thenReturn(list);
		
		List<CustomerResponseDTO> listResponse = List.of();
		
		when(mapper.toResponseList(list)).thenReturn(listResponse);
		List<CustomerResponseDTO> listResult = service.listAll();
		
		assertNotNull(listResult);
		
		assertEquals(0, listResult.size());
		
		verify(repository).findAll();
		verify(mapper).toResponseList(list);

	}
	
}



















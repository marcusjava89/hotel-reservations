package br.com.hotel.hotel_reservations.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.hotel.hotel_reservations.dto.CustomerRequestDTO;
import br.com.hotel.hotel_reservations.dto.CustomerResponseDTO;
import br.com.hotel.hotel_reservations.service.CustomerService;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
	private final CustomerService service;
	
	@PostMapping
	public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDTO request){
		CustomerResponseDTO response = service.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CustomerResponseDTO> findById(@PathVariable Long id){
		CustomerResponseDTO response = service.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/by-email")
	public ResponseEntity<CustomerResponseDTO> findByEmail(@RequestParam String email){
		CustomerResponseDTO response = service.findByEmail(email);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	public ResponseEntity<List<CustomerResponseDTO>> listAll(){
		List<CustomerResponseDTO> customers = service.listAll();
		return ResponseEntity.ok(customers);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CustomerResponseDTO> update(@PathVariable Long id,
			@Valid @RequestBody CustomerRequestDTO request){
		CustomerResponseDTO response = service.updateCustomer(id, request);
		return ResponseEntity.ok(response);
	}
	
}

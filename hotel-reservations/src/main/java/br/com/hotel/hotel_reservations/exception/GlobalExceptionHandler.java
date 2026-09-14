package br.com.hotel.hotel_reservations.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> 
    handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
        	  if (error instanceof FieldError fieldError) {
        	        String field = fieldError.getField();
        	        String message = error.getDefaultMessage();
        	        errors.put(field, message);
        	    }
        });

        log.warn("Validation failed: {}", errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
	
	/*To a general exception.*/
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handlerExeception(Exception ex){
		
		log.error("Unexpected error.", ex);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
	}
	
	@ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException ex) {
		
		log.warn("Customer not found: {}", ex.getMessage());
		
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(CustomerConflictException.class)
	public ResponseEntity<String> handlerCustomerConflictException(CustomerConflictException ex){
		
		log.warn("Customer conflict: {}", ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
	
	@ExceptionHandler(RoomConflictException.class)
	public ResponseEntity<String> handlerRoomConflictException(RoomConflictException ex){
		
		log.warn("Room conflict: {}", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
	
	@ExceptionHandler(RoomNotFoundException.class)
	public ResponseEntity<String> handlerRoomNotFoundException(RoomNotFoundException ex){
		
		log.warn("Room not found: {}", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(InvalidReservationDateException.class)
	public ResponseEntity<String> handlerInvalidReservationDateException(InvalidReservationDateException ex){
		
		log.warn("Invalid reservation date: {}", ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
	@ExceptionHandler(ReservationConflictException.class)
	public ResponseEntity<String> handlerReservationConflictException(ReservationConflictException ex){
		
		log.warn("Reservation conflict: {}", ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
	
	@ExceptionHandler(ReservationNotFoundException.class)
	public ResponseEntity<String> handlerReservationNotFoundException(ReservationNotFoundException ex){
		
		log.warn("Reservation not found: {}", ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(ReservationStatusInvalidException.class)
	public ResponseEntity<String> handlerReservationStatusInvalidException(ReservationStatusInvalidException ex){
		
		log.warn("Reservation status invalid: {}", ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
	@ExceptionHandler(InvalidZipCodeException.class)
	public ResponseEntity<String> handlerInvalidZipCodeException(InvalidZipCodeException ex){
		
		log.warn("Invalid zipcode: {}", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
}

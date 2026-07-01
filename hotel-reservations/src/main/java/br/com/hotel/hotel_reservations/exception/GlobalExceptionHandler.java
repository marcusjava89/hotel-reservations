package br.com.hotel.hotel_reservations.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
	
	@ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(CustomerConflictException.class)
	public ResponseEntity<String> handlerCustomerConflictException(CustomerConflictException ex){
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
	
	@ExceptionHandler(RoomConflictException.class)
	public ResponseEntity<String> handlerRoomConflictException(RoomConflictException ex){
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
	
	@ExceptionHandler(RoomNotFoundException.class)
	public ResponseEntity<String> handlerRoomNotFoundException(RoomNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(InvalidReservationDateException.class)
	public ResponseEntity<String> handlerInvalidReservationDateException(InvalidReservationDateException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
	@ExceptionHandler(ReservationConflictException.class)
	public ResponseEntity<String> handlerReservationConflictException(ReservationConflictException ex){
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
	
	@ExceptionHandler(ReservationNotFoundException.class)
	public ResponseEntity<String> handlerReservationNotFoundException(ReservationNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(ReservationStatusInvalidException.class)
	public ResponseEntity<String> handlerReservationStatusInvalidException(ReservationStatusInvalidException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
}

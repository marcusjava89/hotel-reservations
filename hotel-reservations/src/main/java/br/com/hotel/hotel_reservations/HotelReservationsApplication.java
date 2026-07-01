package br.com.hotel.hotel_reservations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HotelReservationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelReservationsApplication.class, args);
		System.out.println("Connected hotel.");
	}

}

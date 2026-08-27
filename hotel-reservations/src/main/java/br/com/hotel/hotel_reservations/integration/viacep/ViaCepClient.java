package br.com.hotel.hotel_reservations.integration.viacep;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ViaCepClient {
	private final RestClient restClient;
	
	public ViaCepResponse findByZipCode(String zipCode) {
		return restClient.get().uri("https://viacep.com.br/ws/{cep}/json", zipCode).retrieve()
				.body(ViaCepResponse.class);
	}
	
}
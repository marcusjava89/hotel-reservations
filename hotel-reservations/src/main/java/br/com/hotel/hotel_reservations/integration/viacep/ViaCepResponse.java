package br.com.hotel.hotel_reservations.integration.viacep;

public record ViaCepResponse(String cep, String logradouro, String complemento, String bairro, String uf, 
		Boolean erro) {}
package com.example.gerenciadorfisio.application.services;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ViaCepService {

    public String buscarEndereco(String cep) {
        if (cep == null || !cep.replaceAll("\\D", "").matches("\\d{8}")) {
            throw new IllegalArgumentException("CEP inválido!");
        }
        try {
            String cepNumeros = cep.replaceAll("\\D", "");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://viacep.com.br/ws/" + cepNumeros + "/json/"))
                    .GET()
                    .build();

            HttpResponse<String> resposta = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (resposta.statusCode() != 200) {
                throw new IllegalArgumentException("Erro ao consultar CEP!");
            }
            return resposta.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalArgumentException("Consulta de CEP interrompida!");
        } catch (Exception e) {
            throw new IllegalArgumentException("Não foi possível consultar o CEP!");
        }
    }
}

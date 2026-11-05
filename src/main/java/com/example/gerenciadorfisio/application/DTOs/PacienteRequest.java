package com.example.gerenciadorfisio.application.DTOs;

import java.time.LocalDate;
public record PacienteRequest(String nome, String cpf, String telefone, String email,
                              LocalDate dataNascimento, String endereco, String observacoes) {
}

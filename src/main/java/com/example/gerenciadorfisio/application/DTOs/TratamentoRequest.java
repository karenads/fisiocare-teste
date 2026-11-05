package com.example.gerenciadorfisio.application.DTOs;

import java.time.LocalDateTime;
public record TratamentoRequest(String nome, String descricao, LocalDateTime dataInicio,
                                LocalDateTime dataFinal, int totalSessoes,
                                int sessoesRealizadas, Long pacienteId) {
}

package com.example.gerenciadorfisio.application.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;
public record SessaoRequest(LocalDate data, LocalTime horario, String descricao,
                            String observacoes, Boolean realizada, Long tratamentoId) {
}

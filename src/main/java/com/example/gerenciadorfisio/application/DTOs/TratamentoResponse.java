package com.example.gerenciadorfisio.application.DTOs;

import com.example.gerenciadorfisio.domain.entities.Tratamento;
import com.example.gerenciadorfisio.domain.entities.EnumStatusTratamento;
import java.time.LocalDateTime;

public record TratamentoResponse(Long id, String nome, String descricao,
                                 LocalDateTime dataInicio, LocalDateTime dataFinal,
                                 int totalSessoes, int sessoesRealizadas, EnumStatusTratamento status,
                                 Long pacienteId, Long responsavelId) {
    public TratamentoResponse(Tratamento t) {
        this(t.getId(), t.getNome(), t.getDescricao(), t.getDataInicio(), t.getDataFinal(),
                t.getTotalSessoes(), t.getSessoesRealizadas(), t.getStatus(),
                t.getPaciente() == null ? null : t.getPaciente().getId(),
                t.getResponsavel() == null ? null : t.getResponsavel().getId());
    }
}

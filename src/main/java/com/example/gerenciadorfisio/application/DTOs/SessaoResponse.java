package com.example.gerenciadorfisio.application.DTOs;

import com.example.gerenciadorfisio.domain.entities.Sessao;
import com.example.gerenciadorfisio.domain.entities.EnumStatusSessao;
import java.time.LocalDate;
import java.time.LocalTime;

public record SessaoResponse(Long id, LocalDate data, LocalTime horario, String descricao,
                             String observacoes, Boolean realizada, EnumStatusSessao status,
                             Long tratamentoId, Long responsavelId) {
    public SessaoResponse(Sessao s) {
        this(s.getId(), s.getData(), s.getHorario(), s.getDescricao(), s.getObservacoes(),
                s.getRealizada(), s.getStatus(),
                s.getTratamento() == null ? null : s.getTratamento().getId(),
                s.getResponsavel() == null ? null : s.getResponsavel().getId());
    }
}

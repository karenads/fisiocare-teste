package com.example.gerenciadorfisio.application.ports;

import com.example.gerenciadorfisio.application.DTOs.SessaoRequest;
import com.example.gerenciadorfisio.application.DTOs.SessaoResponse;
import com.example.gerenciadorfisio.domain.entities.Sessao;
import com.example.gerenciadorfisio.domain.entities.EnumStatusSessao;
import java.util.List;

public interface SessaoPorta {
    List<SessaoResponse> listarTodos();
    Sessao buscarPorId(Long id);
    SessaoResponse buscarResponsePorId(Long id);
    SessaoResponse salvarSessao(SessaoRequest request);
    boolean atualizar(Long id, SessaoRequest request);
    boolean atualizarStatus(Long id, EnumStatusSessao status);
}

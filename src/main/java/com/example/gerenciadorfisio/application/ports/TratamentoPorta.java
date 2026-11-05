package com.example.gerenciadorfisio.application.ports;

import com.example.gerenciadorfisio.application.DTOs.TratamentoRequest;
import com.example.gerenciadorfisio.application.DTOs.TratamentoResponse;
import com.example.gerenciadorfisio.domain.entities.Tratamento;
import com.example.gerenciadorfisio.domain.entities.EnumStatusTratamento;
import java.util.List;

public interface TratamentoPorta {
    List<TratamentoResponse> listarTodos();
    Tratamento buscarPorId(Long id);
    TratamentoResponse buscarResponsePorId(Long id);
    TratamentoResponse salvarTratamento(TratamentoRequest request);
    boolean atualizar(Long id, TratamentoRequest request);
    boolean atualizarStatus(Long id, EnumStatusTratamento status);
}

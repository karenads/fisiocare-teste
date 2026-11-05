package com.example.gerenciadorfisio.application.ports;

import com.example.gerenciadorfisio.application.DTOs.PacienteRequest;
import com.example.gerenciadorfisio.application.DTOs.PacienteResponse;
import com.example.gerenciadorfisio.domain.entities.Paciente;
import com.example.gerenciadorfisio.domain.entities.EnumStatusPaciente;
import java.util.List;

public interface PacientePorta {
    List<PacienteResponse> listarTodos();
    Paciente buscarPorId(Long id);
    PacienteResponse buscarResponsePorId(Long id);
    PacienteResponse salvarPaciente(PacienteRequest request);
    boolean atualizar(Long id, PacienteRequest request);
    boolean atualizarStatus(Long id, EnumStatusPaciente status);
}

package com.example.gerenciadorfisio.application.DTOs;

import com.example.gerenciadorfisio.domain.entities.Paciente;
import com.example.gerenciadorfisio.domain.entities.EnumStatusPaciente;
import java.time.LocalDate;

public record PacienteResponse(Long id, String nome, String cpf, String telefone, String email,
                               LocalDate dataNascimento, String endereco, String observacoes,
                               EnumStatusPaciente status, Long responsavelId) {
    public PacienteResponse(Paciente paciente) {
        this(paciente.getId(), paciente.getNome(), paciente.getCpf(), paciente.getTelefone(),
                paciente.getEmail(), paciente.getDataNascimento(), paciente.getEndereco(),
                paciente.getObservacoes(), paciente.getStatus(),
                paciente.getResponsavel() == null ? null : paciente.getResponsavel().getId());
    }
}

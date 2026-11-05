package com.example.gerenciadorfisio.application.DTOs;

import com.example.gerenciadorfisio.domain.entities.Usuario;

public record UsuarioResponse(Long id, String nome, String cpf, String email,
                              EnumStatusUsuario status, String role) {
    public UsuarioResponse(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getCpf().toString(),
                usuario.getEmail(), usuario.getStatus(), usuario.getRole());
    }
}

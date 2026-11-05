package com.example.gerenciadorfisio.application.DTOs;

import com.example.gerenciadorfisio.domain.entities.Usuario;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public record UsuarioLogado(Long id, String email, String role,
                            Collection<? extends GrantedAuthority> authorities) {

    public UsuarioLogado(Usuario usuario) {
        this(usuario.getId(), usuario.getEmail(), usuario.getRole(),
                "ROLE_USER".equals(usuario.getRole())
                    ? List.of(new SimpleGrantedAuthority("ROLE_USER"),
                              new SimpleGrantedAuthority("ROLE_RECEPCAO"))
                    : List.of(new SimpleGrantedAuthority(usuario.getRole())));
    }
}

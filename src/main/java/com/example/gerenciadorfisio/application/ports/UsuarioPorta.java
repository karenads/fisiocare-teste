package com.example.gerenciadorfisio.application.ports;

import com.example.gerenciadorfisio.application.DTOs.*;
import com.example.gerenciadorfisio.domain.entities.Usuario;
import java.util.List;

public interface UsuarioPorta {
    LoginResponse validarUsuarioAutenticadoRetornaToken(LoginRequest request);
    UsuarioLogado getUsuarioLogado();
    Usuario getUsuarioEntidadeLogado();
    boolean isAdmin();
    List<UsuarioResponse> listarTodosUsuariosTable();
    CriarAdminResponse criarAdmin(CriarAdminRequest request);
    UsuarioResponse salvarUsuario(UsuarioRequest request, Long id);
    UsuarioResponse buscarPorId(Long id);
    UsuarioResponse buscarUsuarioLogado();
    boolean atualizarStatus(EnumStatusUsuario status, Long id);
    boolean atualizarRole(Long id, String role);
}

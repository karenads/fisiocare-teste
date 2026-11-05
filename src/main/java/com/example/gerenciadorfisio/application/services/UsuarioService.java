package com.example.gerenciadorfisio.application.services;

import com.example.gerenciadorfisio.application.ports.UsuarioPorta;

import com.example.gerenciadorfisio.application.DTOs.*;
import com.example.gerenciadorfisio.domain.entities.Usuario;
import com.example.gerenciadorfisio.domain.repository.UsuarioRepository;
import com.example.gerenciadorfisio.domain.valueobject.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService implements UsuarioPorta {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Value("${spring.secret}")
    private String secret;

    public LoginResponse validarUsuarioAutenticadoRetornaToken(LoginRequest request) {
        if (usuarioRepository.existsUsuarioByEmailAndSenha(request.email(), request.senha())) {
            var usuario = usuarioRepository.findByEmail(request.email()).orElse(null);
            if (usuario != null && usuario.getStatus() == EnumStatusUsuario.ATIVO) {
                return new LoginResponse(tokenService.gerarToken(request.email()));
            }
        }
        return null;
    }

    public UsuarioLogado getUsuarioLogado() {
        return (UsuarioLogado) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Usuario getUsuarioEntidadeLogado() {
        return usuarioRepository.findById(getUsuarioLogado().id()).orElseThrow(
                () -> new IllegalArgumentException("Usuário logado não encontrado!")
        );
    }

    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(getUsuarioLogado().role());
    }

    public List<UsuarioResponse> listarTodosUsuariosTable() {
        return usuarioRepository.findAll().stream().map(UsuarioResponse::new).toList();
    }

    // O JavaFX cria somente o primeiro administrador. Depois, exige login e permissão.
    public CriarAdminResponse criarAdmin(CriarAdminRequest request) {
        if (usuarioRepository.count() != 0) {
            throw new IllegalArgumentException("O administrador inicial já foi cadastrado!");
        }
        if (request.secretKey() == null || !request.secretKey().equals(secret)) {
            throw new IllegalArgumentException("Chave de administrador inválida!");
        }
        Usuario usuario = usuarioRepository.save(new Usuario(request));
        return new CriarAdminResponse(usuario.getId(), "Usuário salvo com sucesso!");
    }

    public UsuarioResponse salvarUsuario(UsuarioRequest request, Long id) {
        Usuario usuario;
        if (id == 0L) {
            usuario = new Usuario(request);
        } else {
            usuario = usuarioRepository.findById(id).orElse(null);
            if (usuario == null) return null;
            usuario.setNome(request.nome());
            usuario.setCpf(new CPF(request.cpf()));
            usuario.setEmail(request.email());
            if (request.senha() != null && !request.senha().isBlank()) {
                usuario.setSenha(request.senha());
            }
        }
        return new UsuarioResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        return usuario == null ? null : new UsuarioResponse(usuario);
    }

    public UsuarioResponse buscarUsuarioLogado() {
        return new UsuarioResponse(getUsuarioEntidadeLogado());
    }

    public boolean atualizarStatus(EnumStatusUsuario status, Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) return false;
        usuario.setStatus(status);
        usuarioRepository.save(usuario);
        return true;
    }

    public boolean atualizarRole(Long id, String role) {
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_FISIOTERAPEUTA".equals(role)
                && !"ROLE_RECEPCAO".equals(role)) {
            throw new IllegalArgumentException("Perfil inválido!");
        }
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) return false;
        usuario.setRole(role);
        usuarioRepository.save(usuario);
        return true;
    }
}

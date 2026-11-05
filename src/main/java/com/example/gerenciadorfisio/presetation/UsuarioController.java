package com.example.gerenciadorfisio.presetation;

import com.example.gerenciadorfisio.application.DTOs.*;
import com.example.gerenciadorfisio.application.ports.UsuarioPorta;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuario")
public class UsuarioController {

    @Autowired
    private UsuarioPorta usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodosUsuariosTable());
    }

    @PostMapping("/admin")
    public ResponseEntity<?> criarAdmin(@RequestBody CriarAdminRequest request) {
        try {
            return ResponseEntity.ok(usuarioService.criarAdmin(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscarusuariologado")
    public ResponseEntity<UsuarioResponse> buscarUsuarioLogado() {
        return ResponseEntity.ok(usuarioService.buscarUsuarioLogado());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.buscarPorId(id);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody UsuarioRequest request) {
        try {
            return ResponseEntity.ok(usuarioService.salvarUsuario(request, 0L));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody UsuarioRequest request) {
        try {
            UsuarioResponse usuario = usuarioService.salvarUsuario(request, id);
            return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id,
                                               @RequestBody AtualizarStatusRequest request) {
        return usuarioService.atualizarStatus(request.status(), id) ?
                ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<?> atualizarRole(@PathVariable Long id,
                                           @RequestBody AtualizarRoleRequest request) {
        try {
            return usuarioService.atualizarRole(id, request.role()) ?
                    ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return usuarioService.atualizarStatus(EnumStatusUsuario.EXCLUIDO, id) ?
                ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}

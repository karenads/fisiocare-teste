package com.example.gerenciadorfisio.presetation;

import com.example.gerenciadorfisio.application.DTOs.SessaoRequest;
import com.example.gerenciadorfisio.application.DTOs.SessaoResponse;
import com.example.gerenciadorfisio.application.DTOs.AtualizarStatusSessaoRequest;
import com.example.gerenciadorfisio.application.ports.SessaoPorta;
import com.example.gerenciadorfisio.domain.entities.EnumStatusSessao;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sessoes")
@Tag(name = "Sessao")
public class SessaoController {

    @Autowired
    private SessaoPorta sessaoService;

    @GetMapping
    public ResponseEntity<List<SessaoResponse>> listarTodos() {
        return ResponseEntity.ok(sessaoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoResponse> buscarPorId(@PathVariable Long id) {
        SessaoResponse resposta = sessaoService.buscarResponsePorId(id);
        return resposta != null ? ResponseEntity.ok(resposta) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody SessaoRequest request) {
        try {
            return ResponseEntity.ok(sessaoService.salvarSessao(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody SessaoRequest request) {
        try {
            return sessaoService.atualizar(id, request) ?
                    ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id,
                                                @RequestBody AtualizarStatusSessaoRequest request) {
        return sessaoService.atualizarStatus(id, request.status()) ?
                ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return sessaoService.atualizarStatus(id, EnumStatusSessao.EXCLUIDO) ?
                ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}

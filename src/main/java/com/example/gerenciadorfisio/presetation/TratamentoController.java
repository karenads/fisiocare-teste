package com.example.gerenciadorfisio.presetation;

import com.example.gerenciadorfisio.application.DTOs.TratamentoRequest;
import com.example.gerenciadorfisio.application.DTOs.TratamentoResponse;
import com.example.gerenciadorfisio.application.DTOs.AtualizarStatusTratamentoRequest;
import com.example.gerenciadorfisio.application.ports.TratamentoPorta;
import com.example.gerenciadorfisio.domain.entities.EnumStatusTratamento;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tratamentos")
@Tag(name = "Tratamento")
public class TratamentoController {

    @Autowired
    private TratamentoPorta tratamentoService;

    @GetMapping
    public ResponseEntity<List<TratamentoResponse>> listarTodos() {
        return ResponseEntity.ok(tratamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TratamentoResponse> buscarPorId(@PathVariable Long id) {
        TratamentoResponse resposta = tratamentoService.buscarResponsePorId(id);
        return resposta != null ? ResponseEntity.ok(resposta) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody TratamentoRequest request) {
        try {
            return ResponseEntity.ok(tratamentoService.salvarTratamento(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody TratamentoRequest request) {
        try {
            return tratamentoService.atualizar(id, request) ?
                    ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id,
                                                @RequestBody AtualizarStatusTratamentoRequest request) {
        return tratamentoService.atualizarStatus(id, request.status()) ?
                ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return tratamentoService.atualizarStatus(id, EnumStatusTratamento.EXCLUIDO) ?
                ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}

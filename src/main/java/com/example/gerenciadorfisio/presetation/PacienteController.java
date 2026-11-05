package com.example.gerenciadorfisio.presetation;

import com.example.gerenciadorfisio.application.DTOs.PacienteRequest;
import com.example.gerenciadorfisio.application.DTOs.PacienteResponse;
import com.example.gerenciadorfisio.application.DTOs.AtualizarStatusPacienteRequest;
import com.example.gerenciadorfisio.application.ports.PacientePorta;
import com.example.gerenciadorfisio.domain.entities.EnumStatusPaciente;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
@Tag(name = "Paciente")
public class PacienteController {

    @Autowired
    private PacientePorta pacienteService;

    @Autowired
    private com.example.gerenciadorfisio.application.services.ViaCepService viaCepService;

    @GetMapping(value = "/cep/{cep}", produces = "application/json")
    public ResponseEntity<?> buscarCep(@PathVariable String cep) {
        try {
            return ResponseEntity.ok(viaCepService.buscarEndereco(cep));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponse>> listarTodos() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        PacienteResponse resposta = pacienteService.buscarResponsePorId(id);
        return resposta != null ? ResponseEntity.ok(resposta) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PacienteRequest request) {
        try {
            return ResponseEntity.ok(pacienteService.salvarPaciente(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody PacienteRequest request) {
        try {
            return pacienteService.atualizar(id, request) ?
                    ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id,
                                                @RequestBody AtualizarStatusPacienteRequest request) {
        return pacienteService.atualizarStatus(id, request.status()) ?
                ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return pacienteService.atualizarStatus(id, EnumStatusPaciente.EXCLUIDO) ?
                ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}

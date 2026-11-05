package com.example.gerenciadorfisio.application.services;

import com.example.gerenciadorfisio.application.ports.PacientePorta;
import com.example.gerenciadorfisio.application.ports.UsuarioPorta;

import com.example.gerenciadorfisio.application.DTOs.PacienteRequest;
import com.example.gerenciadorfisio.application.DTOs.PacienteResponse;
import com.example.gerenciadorfisio.domain.entities.Paciente;
import com.example.gerenciadorfisio.domain.entities.EnumStatusPaciente;
import com.example.gerenciadorfisio.domain.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PacienteService implements PacientePorta {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioPorta usuarioService;

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodos() {
        List<Paciente> pacientes = usuarioService.isAdmin()
                ? pacienteRepository.findAll()
                : pacienteRepository.findByResponsavel_Id(usuarioService.getUsuarioLogado().id());
        return pacientes.stream().map(PacienteResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public Paciente buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id).orElse(null);
        if (paciente == null) return null;
        if (usuarioService.isAdmin()) return paciente;
        if (paciente.getResponsavel() != null
                && paciente.getResponsavel().getId().equals(usuarioService.getUsuarioLogado().id())) {
            return paciente;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarResponsePorId(Long id) {
        Paciente paciente = buscarPorId(id);
        return paciente == null ? null : new PacienteResponse(paciente);
    }

    private void preencher(Paciente paciente, PacienteRequest request) {
        paciente.setNome(request.nome());
        paciente.setCpf(request.cpf());
        paciente.setTelefone(request.telefone());
        paciente.setEmail(request.email());
        paciente.setDataNascimento(request.dataNascimento());
        paciente.setEndereco(request.endereco());
        paciente.setObservacoes(request.observacoes());
        if (paciente.getNome() == null || paciente.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome obrigatório!");
        }
    }

    @Transactional
    public PacienteResponse salvarPaciente(PacienteRequest request) {
        Paciente paciente = new Paciente();
        preencher(paciente, request);
        paciente.setResponsavel(usuarioService.getUsuarioEntidadeLogado());
        return new PacienteResponse(pacienteRepository.save(paciente));
    }

    @Transactional
    public boolean atualizar(Long id, PacienteRequest request) {
        Paciente paciente = buscarPorId(id);
        if (paciente == null) return false;
        preencher(paciente, request);
        pacienteRepository.save(paciente);
        return true;
    }

    @Transactional
    public boolean atualizarStatus(Long id, EnumStatusPaciente status) {
        Paciente paciente = buscarPorId(id);
        if (paciente == null) return false;
        paciente.setStatus(status);
        pacienteRepository.save(paciente);
        return true;
    }
}

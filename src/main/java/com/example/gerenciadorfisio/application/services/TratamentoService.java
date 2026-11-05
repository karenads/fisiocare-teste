package com.example.gerenciadorfisio.application.services;

import com.example.gerenciadorfisio.application.ports.TratamentoPorta;
import com.example.gerenciadorfisio.application.ports.UsuarioPorta;
import com.example.gerenciadorfisio.application.ports.PacientePorta;

import com.example.gerenciadorfisio.application.DTOs.TratamentoRequest;
import com.example.gerenciadorfisio.application.DTOs.TratamentoResponse;
import com.example.gerenciadorfisio.domain.entities.Tratamento;
import com.example.gerenciadorfisio.domain.entities.Paciente;
import com.example.gerenciadorfisio.domain.entities.EnumStatusTratamento;
import com.example.gerenciadorfisio.domain.repository.TratamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TratamentoService implements TratamentoPorta {

    @Autowired
    private TratamentoRepository tratamentoRepository;
    @Autowired
    private UsuarioPorta usuarioService;
    @Autowired
    private PacientePorta pacienteService;

    @Transactional(readOnly = true)
    public List<TratamentoResponse> listarTodos() {
        List<Tratamento> tratamentos = usuarioService.isAdmin()
                ? tratamentoRepository.findAll()
                : tratamentoRepository.findByResponsavel_Id(usuarioService.getUsuarioLogado().id());
        return tratamentos.stream().map(TratamentoResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public Tratamento buscarPorId(Long id) {
        Tratamento t = tratamentoRepository.findById(id).orElse(null);
        if (t == null) return null;
        if (usuarioService.isAdmin()) return t;
        if (t.getResponsavel() != null
                && t.getResponsavel().getId().equals(usuarioService.getUsuarioLogado().id())) {
            return t;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public TratamentoResponse buscarResponsePorId(Long id) {
        Tratamento t = buscarPorId(id);
        return t == null ? null : new TratamentoResponse(t);
    }

    private void preencher(Tratamento t, TratamentoRequest request) {
        if (request.pacienteId() == null) {
            throw new IllegalArgumentException("Informe o paciente do tratamento!");
        }
        Paciente paciente = pacienteService.buscarPorId(request.pacienteId());
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente não encontrado ou sem permissão!");
        }
        t.setPaciente(paciente);
        t.setNome(request.nome());
        t.setDescricao(request.descricao());
        t.setDataInicio(request.dataInicio());
        t.setDataFinal(request.dataFinal());
        t.setTotalSessoes(request.totalSessoes());
        t.setSessoesRealizadas(request.sessoesRealizadas());
        t.validarTratamento();
    }

    @Transactional
    public TratamentoResponse salvarTratamento(TratamentoRequest request) {
        Tratamento t = new Tratamento();
        preencher(t, request);
        t.setResponsavel(usuarioService.getUsuarioEntidadeLogado());
        return new TratamentoResponse(tratamentoRepository.save(t));
    }

    @Transactional
    public boolean atualizar(Long id, TratamentoRequest request) {
        Tratamento t = buscarPorId(id);
        if (t == null) return false;
        preencher(t, request);
        tratamentoRepository.save(t);
        return true;
    }

    @Transactional
    public boolean atualizarStatus(Long id, EnumStatusTratamento status) {
        Tratamento t = buscarPorId(id);
        if (t == null) return false;
        t.setStatus(status);
        tratamentoRepository.save(t);
        return true;
    }
}

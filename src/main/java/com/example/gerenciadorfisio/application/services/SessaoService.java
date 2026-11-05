package com.example.gerenciadorfisio.application.services;

import com.example.gerenciadorfisio.application.ports.SessaoPorta;
import com.example.gerenciadorfisio.application.ports.UsuarioPorta;
import com.example.gerenciadorfisio.application.ports.TratamentoPorta;

import com.example.gerenciadorfisio.application.DTOs.SessaoRequest;
import com.example.gerenciadorfisio.application.DTOs.SessaoResponse;
import com.example.gerenciadorfisio.domain.entities.Sessao;
import com.example.gerenciadorfisio.domain.entities.Tratamento;
import com.example.gerenciadorfisio.domain.entities.EnumStatusSessao;
import com.example.gerenciadorfisio.domain.repository.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SessaoService implements SessaoPorta {

    @Autowired
    private SessaoRepository sessaoRepository;
    @Autowired
    private UsuarioPorta usuarioService;
    @Autowired
    private TratamentoPorta tratamentoService;

    @Transactional(readOnly = true)
    public List<SessaoResponse> listarTodos() {
        List<Sessao> sessoes = usuarioService.isAdmin()
                ? sessaoRepository.findAll()
                : sessaoRepository.findByResponsavel_Id(usuarioService.getUsuarioLogado().id());
        return sessoes.stream().map(SessaoResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public Sessao buscarPorId(Long id) {
        Sessao s = sessaoRepository.findById(id).orElse(null);
        if (s == null) return null;
        if (usuarioService.isAdmin()) return s;
        if (s.getResponsavel() != null
                && s.getResponsavel().getId().equals(usuarioService.getUsuarioLogado().id())) {
            return s;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public SessaoResponse buscarResponsePorId(Long id) {
        Sessao s = buscarPorId(id);
        return s == null ? null : new SessaoResponse(s);
    }

    private void preencher(Sessao s, SessaoRequest request) {
        if (request.tratamentoId() == null) {
            throw new IllegalArgumentException("Informe o tratamento da sessão!");
        }
        Tratamento t = tratamentoService.buscarPorId(request.tratamentoId());
        if (t == null) {
            throw new IllegalArgumentException("Tratamento não encontrado ou sem permissão!");
        }
        s.setTratamento(t);
        s.setData(request.data());
        s.setHorario(request.horario());
        s.setDescricao(request.descricao());
        s.setObservacoes(request.observacoes());
        s.setRealizada(request.realizada());
        s.validarSessao();
    }

    @Transactional
    public SessaoResponse salvarSessao(SessaoRequest request) {
        Sessao s = new Sessao();
        preencher(s, request);
        s.setResponsavel(usuarioService.getUsuarioEntidadeLogado());
        return new SessaoResponse(sessaoRepository.save(s));
    }

    @Transactional
    public boolean atualizar(Long id, SessaoRequest request) {
        Sessao s = buscarPorId(id);
        if (s == null) return false;
        preencher(s, request);
        sessaoRepository.save(s);
        return true;
    }

    @Transactional
    public boolean atualizarStatus(Long id, EnumStatusSessao status) {
        Sessao s = buscarPorId(id);
        if (s == null) return false;
        s.setStatus(status);
        sessaoRepository.save(s);
        return true;
    }
}

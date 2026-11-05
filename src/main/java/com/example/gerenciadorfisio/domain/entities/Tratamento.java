package com.example.gerenciadorfisio.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tratamento {

    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFinal;
    private int totalSessoes;
    private int sessoesRealizadas;
    private EnumStatusTratamento status = EnumStatusTratamento.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    // O Set representa as sessões relacionadas a este tratamento.
    @OneToMany(mappedBy = "tratamento")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Sessao> sessoes = new HashSet<>();

    public void validarTratamento() {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do tratamento obrigatório!");
        }
        if (totalSessoes < 0 || sessoesRealizadas < 0 || sessoesRealizadas > totalSessoes) {
            throw new IllegalArgumentException("Quantidade de sessões inválida!");
        }
        if (dataInicio != null && dataFinal != null && dataFinal.isBefore(dataInicio)) {
            throw new IllegalArgumentException("Data final não pode ser anterior à data de início!");
        }
        if (paciente == null) {
            throw new IllegalArgumentException("Informe o paciente do tratamento!");
        }
    }
}

package com.example.gerenciadorfisio.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sessao {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDate data;
    private LocalTime horario;
    private String descricao;
    private String observacoes;
    private Boolean realizada;
    private EnumStatusSessao status = EnumStatusSessao.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tratamento_id")
    private Tratamento tratamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    public void validarSessao() {
        if (data == null || horario == null) {
            throw new IllegalArgumentException("Informe a data e o horário da sessão!");
        }
        if (tratamento == null) {
            throw new IllegalArgumentException("Informe o tratamento da sessão!");
        }
    }
}

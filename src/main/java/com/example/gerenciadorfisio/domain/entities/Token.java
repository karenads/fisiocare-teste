package com.example.gerenciadorfisio.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 2048)
    private String token;

    private LocalDateTime dataCadastro;

    private LocalDateTime expiracao;

    private boolean revogado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Token(String token, Usuario usuario, LocalDateTime expiracao) {
        this.token = token;
        this.usuario = usuario;
        this.expiracao = expiracao;
        this.dataCadastro = LocalDateTime.now();
    }
}

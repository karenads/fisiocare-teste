package com.example.gerenciadorfisio.application.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.gerenciadorfisio.application.DTOs.UsuarioLogado;
import com.example.gerenciadorfisio.domain.entities.Token;
import com.example.gerenciadorfisio.domain.entities.Usuario;
import com.example.gerenciadorfisio.domain.repository.TokenRepository;
import com.example.gerenciadorfisio.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TokenService {

    @Value("${spring.secret}")
    private String secret;

    @Value("${spring.expiracao}")
    private Long expiracao;

    @Value("${spring.emissor}")
    private String emissor;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public String gerarToken(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Usuário não encontrado!")
        );

        if (usuario.getStatus() != com.example.gerenciadorfisio.application.DTOs.EnumStatusUsuario.ATIVO) {
            throw new IllegalArgumentException("Usuário bloqueado ou excluído!");
        }

        Instant dataExpiracao = Instant.now().plusSeconds(expiracao * 60);
        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create()
                .withIssuer(emissor)
                .withSubject(email)
                .withJWTId(java.util.UUID.randomUUID().toString())
                .withExpiresAt(dataExpiracao)
                .sign(algorithm);

        LocalDateTime dataLocal = LocalDateTime.ofInstant(dataExpiracao, ZoneId.systemDefault());
        tokenRepository.save(new Token(token, usuario, dataLocal));
        return token;
    }

    @Transactional(readOnly = true)
    public UsuarioLogado verificarToken(String token) {

        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verificador = JWT.require(algorithm)
                .withIssuer(emissor)
                .build();

        DecodedJWT jwt = verificador.verify(token);

        Token tokenBanco = tokenRepository.findTokenByToken(token).orElseThrow(
                () -> new IllegalArgumentException("Token não encontrado!")
        );

        if (tokenBanco.isRevogado() || tokenBanco.getExpiracao().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expirado ou revogado!");
        }

        Usuario usuario = tokenBanco.getUsuario();
        if (!usuario.getEmail().equals(jwt.getSubject())
                || usuario.getStatus() != com.example.gerenciadorfisio.application.DTOs.EnumStatusUsuario.ATIVO) {
            throw new IllegalArgumentException("Usuário não autorizado!");
        }

        return new UsuarioLogado(usuario);
    }

    @Transactional
    public void revogarToken(String token) {
        Token tokenBanco = tokenRepository.findTokenByToken(token).orElse(null);
        if (tokenBanco != null) {
            tokenBanco.setRevogado(true);
            tokenRepository.save(tokenBanco);
        }
    }
}

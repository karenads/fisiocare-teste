package com.example.gerenciadorfisio.domain.entities;

import com.example.gerenciadorfisio.application.DTOs.CriarAdminRequest;
import com.example.gerenciadorfisio.application.DTOs.UsuarioRequest;
import com.example.gerenciadorfisio.application.DTOs.EnumStatusUsuario;
import com.example.gerenciadorfisio.domain.valueobject.CPF;
import com.example.gerenciadorfisio.domain.valueobject.Email;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Embedded
    private CPF cpf;

    private String email;

    private String senha;

    private String role = "ROLE_RECEPCAO";

    private EnumStatusUsuario status = EnumStatusUsuario.ATIVO;

    public void setEmail(String email) {
        this.email = new Email(email).toString();
    }

    public List<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    public Usuario(CriarAdminRequest request) {
        this.setCpf(new CPF(request.cpf()));
        this.setNome(request.nome());
        this.setSenha(request.senha());
        this.setEmail(request.email());
        this.setRole("ROLE_ADMIN");
    }

    public Usuario(UsuarioRequest request) {
        this.setCpf(new CPF(request.cpf()));
        this.setNome(request.nome());
        this.setSenha(request.senha());
        this.setEmail(request.email());
        this.setRole("ROLE_RECEPCAO");
    }
}

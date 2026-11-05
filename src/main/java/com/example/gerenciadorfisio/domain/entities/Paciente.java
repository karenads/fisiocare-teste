package com.example.gerenciadorfisio.domain.entities;

import com.example.gerenciadorfisio.domain.valueobject.CPF;
import com.example.gerenciadorfisio.domain.valueobject.Email;
import com.example.gerenciadorfisio.domain.valueobject.Telefone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private LocalDate dataNascimento;
    private String endereco;
    private String observacoes;
    private EnumStatusPaciente status = EnumStatusPaciente.ATIVO;

    // O usuário responsável vem do token, nunca do formulário.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    // Um paciente pode ter vários tratamentos de fisioterapia.
    @OneToMany(mappedBy = "paciente")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Tratamento> tratamentos = new HashSet<>();

    public void setCpf(String cpf) {
        this.cpf = new CPF(cpf).toString();
    }

    public void setEmail(String email) {
        this.email = new Email(email).toString();
    }

    public void setTelefone(String telefone) {
        this.telefone = new Telefone(telefone).toString();
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        if (dataNascimento != null && dataNascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento inválida!");
        }
        this.dataNascimento = dataNascimento;
    }
}

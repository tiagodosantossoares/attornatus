package com.attornatus.test.entity;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;

@Entity
@Table(name="endereco")

public class Endereco{
    @Id
    @SequenceGenerator(name="seq_endereco",sequenceName = "seq_endereco",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "seq_endereco")
    private Long Id;
    @Column(name = "logradouro",nullable = false)
    private String logradouro;
    @Column(name = "cep",nullable = false)
    private String cep;
    @Column(name = "numero",nullable = false)

    private Integer numero;
    @Column(name = "cidade",nullable = false)
    private String cidade;
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    private Pessoa pessoa;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}
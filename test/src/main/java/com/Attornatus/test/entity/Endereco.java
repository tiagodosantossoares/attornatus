package com.attornatus.test.entity;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.springframework.context.annotation.Lazy;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="endereco")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "endereco_id",scope = Endereco.class)
public class Endereco implements Serializable {
    public Endereco(){}
    public Endereco(Long id, String logradouro, String cep, Integer numero, String cidade, Boolean enderecoPrincipal, Pessoa pessoa) {
        this.id = id;
        this.logradouro = logradouro;
        this.cep = cep;
        this.numero = numero;
        this.cidade = cidade;
        this.enderecoPrincipal = enderecoPrincipal;
        this.pessoa = pessoa;
    }

    @Id
    @SequenceGenerator(name="seq_endereco",sequenceName = "seq_endereco",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "seq_endereco")
    @JsonProperty("endereco_id")
    private Long id;
    @Column(name = "logradouro",nullable = false)
    private String logradouro;
    @Column(name = "cep",nullable = false)
    private String cep;
    @Column(name = "numero",nullable = false)

    private Integer numero;
    @Column(name = "cidade",nullable = false)
    private String cidade;
    @Column(name = "endereco_principal",nullable = false)
    private Boolean enderecoPrincipal;
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("pessoa_id")
    private Pessoa pessoa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {

        this.id = id;
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

    public Boolean getEnderecoPrincipal() {
        return enderecoPrincipal;
    }

    public void setEnderecoPrincipal(Boolean enderecoPrincipal) {
        this.enderecoPrincipal = enderecoPrincipal;
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "Id=" + id +
                ", logradouro='" + logradouro + '\'' +
                ", cep='" + cep + '\'' +
                ", numero=" + numero +
                ", cidade='" + cidade + '\'' +
                ", enderecoPrincipal=" + enderecoPrincipal +
                ", pessoa=" + pessoa +
                '}';
    }
}
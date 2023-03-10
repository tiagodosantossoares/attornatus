package com.attornatus.test.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name="pessoa")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pessoa_id",scope = Pessoa.class)

public class Pessoa implements Serializable {
    public Pessoa(){}
    public Pessoa(Long id, String nome, Date dataNascimento, List<Endereco> enderecos) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.enderecos = enderecos;
    }

    @Id
    @SequenceGenerator(name="seq_pessoa",sequenceName = "seq_pessoa",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "seq_pessoa")
    @JsonProperty("pessoa_id")
    private Long id;
    private String nome;
    @Column(name ="data_nasciment" )
    private Date dataNascimento;
    @OneToMany(mappedBy="pessoa", cascade= CascadeType.REFRESH,fetch = FetchType.LAZY)
    private List<Endereco> enderecos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataNascimento=" + dataNascimento +
                '}';
    }
}
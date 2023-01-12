package com.attornatus.test.repository;

import com.attornatus.test.entity.Endereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco,Long> {
    List<Endereco> findEnderecoByPessoaIdAndLogradouroAndCepAndNumeroAndCidade(Long id,String logradouro,String cep,Integer numero,String cidade);


    Page<Endereco> findAllByPessoaId(Long id, Pageable pageable);

    List<Endereco> findEnderecoByPessoaId(long l);
}
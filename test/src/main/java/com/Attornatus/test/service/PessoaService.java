package com.attornatus.test.service;

import com.attornatus.test.entity.Endereco;
import com.attornatus.test.entity.Pessoa;
import com.attornatus.test.repository.EnderecoRepository;
import com.attornatus.test.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {
    @Autowired
    PessoaRepository pessoaRepository;
    @Autowired
    EnderecoRepository enderecoRepository;

    public ResponseEntity<Object> getPessoaById(Long id) {
        Optional<Pessoa> result=pessoaRepository.findById(id);
        if(result.isPresent()) {
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        }else{
            return  new ResponseEntity<Object>( HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> getPessoas(Pageable pageable) {
        Page<Pessoa> result=pessoaRepository.findAll(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<Object> updatePessoa(Pessoa pessoa) {
        Optional<Pessoa> usuarioSalvo=pessoaRepository.findById(pessoa.getId());
        if(usuarioSalvo.isPresent()) {
            pessoa.setId(pessoa.getId());
            pessoaRepository.save(pessoa);
            return new ResponseEntity<Object>(pessoa, HttpStatus.OK);
        }else{
            return  new ResponseEntity<Object>( HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> createPessoa(Pessoa pessoa) {
        Optional<Pessoa> usuarioSalvo=pessoaRepository.findById(pessoa.getId());
        Optional<Pessoa> usuarioSalvoByName=pessoaRepository.findByNome(pessoa.getNome());
        if(!usuarioSalvo.isPresent() && !usuarioSalvoByName.isPresent()) {
            pessoa.setId(pessoa.getId());
            pessoaRepository.save(pessoa);
            return new ResponseEntity<Object>(pessoa, HttpStatus.OK);
        }else{
            return  new ResponseEntity<Object>("Pessoa com o mesmo nome ja cadastrada.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> removePessoa(Long id) {
        try {
            Optional<Pessoa> usuarioSalvo = pessoaRepository.findById(id);
            if (usuarioSalvo.isPresent()) {
                List<Endereco> enderecos = usuarioSalvo.get().getEnderecos();
                enderecoRepository.deleteAll(enderecos);
                pessoaRepository.delete(usuarioSalvo.get());
            }
            return new ResponseEntity<Object>(HttpStatus.OK);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(HttpStatus.BAD_GATEWAY);
        }
    }
}

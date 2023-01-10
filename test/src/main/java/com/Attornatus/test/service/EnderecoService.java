package com.attornatus.test.service;

import com.attornatus.test.entity.Endereco;
import com.attornatus.test.entity.Pessoa;
import com.attornatus.test.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    @Autowired
    EnderecoRepository enderecoRepository;
    public ResponseEntity<Endereco> getEnderecoById(Long id) {
        Optional<Endereco> endereco=enderecoRepository.findById(id);
        if(endereco.isPresent()){
            return  new ResponseEntity<>(endereco.get(),HttpStatus.OK);
        }else{
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public Page<Endereco> getEnderecoByIdPessoa(Long id,Pageable pageable) {
        return enderecoRepository.findAllByPessoaId(id,pageable);
    }

    public Page<Endereco> getEnderecos(Pageable pageable) {
        return enderecoRepository.findAll(pageable);
    }

    public ResponseEntity<Object> updateEndereco(Endereco endereco) {
        Optional<Endereco> enderecoSalvo=enderecoRepository.findById(endereco.getId());
        if(enderecoSalvo.isPresent()) {
            Endereco enderecoSalvoObj=enderecoSalvo.get();
            endereco.setPessoa(enderecoSalvoObj.getPessoa());
            enderecoRepository.save(endereco);
            return new ResponseEntity<Object>(endereco, HttpStatus.OK);
        }else{
            return  new ResponseEntity<Object>( HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> createEndereco(Endereco endereco) {
        List<Endereco> enderecos=enderecoRepository.findEnderecoByPessoaIdAndLogradouroAndCepAndNumeroAndCidade(
                endereco.getPessoa().getId(),
                endereco.getLogradouro(),
                endereco.getCep(),
                endereco.getNumero(),
                endereco.getCidade());
        if(!enderecos.isEmpty()){
            return new ResponseEntity<>("Já possui endereço cadastrado para essa pessoa.",HttpStatus.BAD_REQUEST);
        }
        Endereco enderecoSalvo=enderecoRepository.save(endereco);
        return new ResponseEntity<Object>(enderecoSalvo, HttpStatus.OK);

    }

    public ResponseEntity<Endereco> removeEndereco(Long id) {
        enderecoRepository.deleteById(id);
        return  new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


}

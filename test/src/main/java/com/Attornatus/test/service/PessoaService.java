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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PessoaService {
    @Autowired
    PessoaRepository pessoaRepository;
    @Autowired
    EnderecoRepository enderecoRepository;
    @Autowired
    EnderecoService enderecoService;
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

    public Pessoa getPessoaComTodosEnderecos(Pessoa pessoaSalva,Pessoa novaPessoa){
        Endereco aux=null;
        List<Endereco> enderecosPessoaSalva=pessoaSalva.getEnderecos();
        List<Endereco> enderecosNovaPessoa=novaPessoa.getEnderecos();
        List<Endereco> setEndereco = new ArrayList<>();
        setEndereco.addAll(enderecosNovaPessoa);
        for(Endereco e:enderecosPessoaSalva){
            if(setEndereco.stream().filter(item->item.getId()==e.getId()).map(item->item).findFirst().orElse(null)==null){
                setEndereco.add(e);
            }
        }


        pessoaSalva.setEnderecos(setEndereco.stream().collect(Collectors.toList()));
        return pessoaSalva;
    }
    @Transactional
    public ResponseEntity<Object> updatePessoa(Pessoa pessoa) {
        Optional<Pessoa> pessoaSalva=pessoaRepository.findById(pessoa.getId());
        Date now=new Date();
        if(now.before(pessoa.getDataNascimento())){
            return  new ResponseEntity<Object>("{\"error\":\"Data de nascimento Invalida.\"}", HttpStatus.BAD_REQUEST);
        }
        if(!pessoaSalva.isPresent()){
            return  new ResponseEntity<Object>("{\"error\":\"Id não Localizado.\"}", HttpStatus.NOT_FOUND);
        }
        ResponseEntity<Object> resultVerificacao=enderecoService.verificaEnderecoPessoa(getPessoaComTodosEnderecos(pessoaSalva.get(),pessoa));
        if(resultVerificacao!=null){
            return resultVerificacao;
        }
        if(pessoaSalva.isPresent()) {
            pessoa.setId(pessoaSalva.get().getId());
            pessoaRepository.save(pessoa);
            Long idEnderecoDefault=0L;
            if(pessoa.getEnderecos()!=null) {
                for(Endereco item :pessoa.getEnderecos()){
                    item.setPessoa(pessoa);
                   enderecoRepository.save(item);
                   if(item.getEnderecoPrincipal()==true){
                       idEnderecoDefault=item.getId();
                   }
                }
            }
            List<Endereco> enderecos=enderecoRepository.findEnderecoByPessoaId(pessoa.getId());
            for(Endereco endereco : enderecos){
                if(idEnderecoDefault!=0L && idEnderecoDefault!=endereco.getId() && endereco.getEnderecoPrincipal()==true){
                    endereco.setEnderecoPrincipal(false);
                    enderecoRepository.save(endereco);
                }
            }
            pessoa.setEnderecos(enderecos);
            return new ResponseEntity<Object>(pessoa, HttpStatus.OK);
        }else{
            return  new ResponseEntity<Object>( HttpStatus.NOT_FOUND);
        }
    }
    @Transactional
    public ResponseEntity<Object> createPessoa(Pessoa pessoa) {
        pessoa.setId(null);
        Optional<Pessoa> usuarioSalvoByName=pessoaRepository.findByNome(pessoa.getNome());

        Date now=new Date();
        if(now.before(pessoa.getDataNascimento())){
            return  new ResponseEntity<Object>("{\"error\":\"Data de nascimento Invalida.\"}", HttpStatus.BAD_REQUEST);
        }
        int qntEnderecoDefault=0;
        if(pessoa.getEnderecos()!=null) {
            for(Endereco item :pessoa.getEnderecos()){
                if(item.getEnderecoPrincipal()==true) {
                    qntEnderecoDefault++;
                }
            }
        }
        if(qntEnderecoDefault>1){
            return  new ResponseEntity<Object>("{\"error\":\"Foi informado mais de 1 endereço como padrão.\"}", HttpStatus.BAD_REQUEST);
        }
        if( !usuarioSalvoByName.isPresent()) {
            pessoa.setId(pessoa.getId());
            pessoaRepository.save(pessoa);
            if(pessoa.getEnderecos()!=null) {
                pessoa.getEnderecos().forEach(item -> {
                    item.setPessoa(pessoa);
                    enderecoRepository.save(item);

                });
            }
            return new ResponseEntity<Object>(pessoa, HttpStatus.CREATED);
        }else{
            return  new ResponseEntity<Object>("{\"error\":\"Pessoa com o mesmo nome já cadastrada.\"}", HttpStatus.BAD_REQUEST);
        }
    }
    @Transactional
    public ResponseEntity<Object> removePessoa(Long id) {
            Optional<Pessoa> usuarioSalvo = pessoaRepository.findById(id);
            if (usuarioSalvo.isPresent()) {
                List<Endereco> enderecos = usuarioSalvo.get().getEnderecos();
                enderecoRepository.deleteAll(enderecos);
                pessoaRepository.delete(usuarioSalvo.get());
            }
            return new ResponseEntity<Object>(HttpStatus.ACCEPTED);

    }

}

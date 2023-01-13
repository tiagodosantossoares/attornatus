package com.attornatus.test.service;

import com.attornatus.test.entity.Endereco;
import com.attornatus.test.entity.Pessoa;
import com.attornatus.test.repository.EnderecoRepository;
import com.attornatus.test.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class EnderecoService {

    @Autowired
    EnderecoRepository enderecoRepository;
    @Autowired
    PessoaRepository pessoaRepository;
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

    @Transactional
    public ResponseEntity<Object> updateEndereco(Endereco endereco) {
        Optional<Endereco> enderecoSalvo=enderecoRepository.findById(endereco.getId());
        if(enderecoSalvo.isPresent()) {
            Optional<Pessoa> pessoa=pessoaRepository.findByEnderecosId(endereco.getId());
            if(!pessoa.isPresent()){
                return  new ResponseEntity<Object>("{\"error\":\"Pessoa informada não foi localizada.\"}", HttpStatus.BAD_REQUEST);
            }

            if(!verificaEndereco(pessoa.get(),endereco)){
                return new ResponseEntity<>("Esse endereço já esta cadastrado para essa pessoa.",HttpStatus.BAD_REQUEST);
            }
            updateEnderecoPrincipal(pessoa.get(), endereco);
            Endereco enderecoSalvoObj=enderecoSalvo.get();
            endereco.setPessoa(enderecoSalvoObj.getPessoa());
            enderecoRepository.save(endereco);
            return new ResponseEntity<Object>(endereco, HttpStatus.OK);
        }else{
            return  new ResponseEntity<Object>( HttpStatus.NOT_FOUND);
        }
    }
    public Boolean verificaEndereco(Pessoa pessoa,Endereco endereco){

        if(endereco.getId()==null){
            List<Endereco> enderecos=enderecoRepository.findByPessoaIdAndLogradouroAndCepAndNumeroAndCidade(
                    pessoa.getId(),
                    endereco.getLogradouro(),
                    endereco.getCep(),
                    endereco.getNumero(),
                    endereco.getCidade());
            if(!enderecos.isEmpty()){
                return false;
            }
        }else{
            List<Endereco> enderecos=enderecoRepository.findByPessoaIdAndLogradouroAndCepAndNumeroAndCidadeAndIdNotLike(
                    pessoa.getId(),
                    endereco.getLogradouro(),
                    endereco.getCep(),
                    endereco.getNumero(),
                    endereco.getCidade(),endereco.getId());
            if(!enderecos.isEmpty()){
                return false;
            }
        }

    return true;
    }
    public ResponseEntity<Object> verificaEnderecoPessoa(Pessoa pessoa){
        int qntEnderecoDefault=0;
        if(pessoa.getEnderecos()!=null) {
            for(Endereco item :pessoa.getEnderecos()){
                if(item.getEnderecoPrincipal()==true) {
                    qntEnderecoDefault++;
                }
                boolean result=verificaEndereco(pessoa,item);
                if(result==false){
                    return new ResponseEntity<>("Endereco ja encotra-se cadastrado para essa pessoa,"+item,HttpStatus.BAD_REQUEST);
                }

            }
        }
        if(qntEnderecoDefault>1){
            return  new ResponseEntity<Object>("{\"error\":\"Foi informado mais de 1 endereço como padrão.\"}", HttpStatus.BAD_REQUEST);
        }
        return null;
    }
    public  void updateEnderecoPrincipal(Pessoa pessoa,Endereco endereco){

        if(pessoa.getEnderecos()!=null && endereco.getEnderecoPrincipal()==true) {
            for(Endereco item :pessoa.getEnderecos()){
                if(endereco.getId()!=null&&item.getId()!=endereco.getId() && item.getEnderecoPrincipal()==true){
                    item.setEnderecoPrincipal(false);
                    enderecoRepository.save(item);
                }else if(endereco.getId()==null&& item.getEnderecoPrincipal()==true){
                    item.setEnderecoPrincipal(false);
                    enderecoRepository.save(item);
                }

            }
        }
    }
    @Transactional
    public ResponseEntity<Object> createEndereco(Long idPessoa, Endereco endereco) {
        if(idPessoa==null){
            return  new ResponseEntity<Object>("{\"error\":\"Necessário informar a pessoa a quem o endereco pertence.\"}", HttpStatus.BAD_REQUEST);
        }
        Optional<Pessoa> pessoa=pessoaRepository.findById(idPessoa);
        if(!pessoa.isPresent()){
            return  new ResponseEntity<Object>("{\"error\":\"Pessoa informada não foi localizada.\"}", HttpStatus.BAD_REQUEST);
        }

        if(!verificaEndereco(pessoa.get(),endereco)){
            return new ResponseEntity<>("Esse endereço já esta cadastrado para essa pessoa.",HttpStatus.BAD_REQUEST);
        }
        updateEnderecoPrincipal(pessoa.get(),endereco);
        endereco.setPessoa(pessoa.get());
        Endereco enderecoSalvo=enderecoRepository.save(endereco);
        return new ResponseEntity<Object>(enderecoSalvo, HttpStatus.OK);

    }

    public ResponseEntity<Endereco> removeEndereco(Long id) {
        enderecoRepository.deleteById(id);
        return  new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


}

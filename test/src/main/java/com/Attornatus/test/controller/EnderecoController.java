package com.attornatus.test.controller;

import com.attornatus.test.entity.Endereco;
import com.attornatus.test.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/endereco", produces="application/json")

public class EnderecoController {


    EnderecoService enderecoService;
    @Autowired
    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }
    /*
     * Retorna a endereco pelo id informado
     * */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getEnderecoById(@PathVariable Long id){
        return enderecoService.getEnderecoById(id);
    }
    /*
     * Retorna a endereco pelo id da pessoa
     * */
    @GetMapping("/pessoa/{pessoa_id}")
    public Page<Endereco> getEnderecoByPessoa(@PathVariable("pessoa_id") Long id, Pageable pageable){
        return enderecoService.getEnderecoByIdPessoa(id,pageable);
    }
    /*
     * Retorna a lista de enderecos cadastradas
     * */
    @GetMapping
    public Page<Endereco> getEnderecos(Pageable pageable){
        return enderecoService.getEnderecos(pageable);
    }
    /*
     * Atualiza a endereco informada
     * */
    @PutMapping
    public ResponseEntity<Object> updateEndereco(@RequestBody Endereco endereco){
        return enderecoService.updateEndereco(endereco);
    }
    /*
     * Cria a endereco informada
     * */
    @PostMapping("/pessoa/{id}")
    public ResponseEntity<Object> createEndereco(@PathVariable(value = "id") Long idPessoa,@RequestBody Endereco endereco){
        return enderecoService.createEndereco(idPessoa,endereco);
    }
    /*
     * remove a endereco informada pelo id
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> createEndereco(@PathVariable Long id){
        return enderecoService.removeEndereco(id);
    }
}

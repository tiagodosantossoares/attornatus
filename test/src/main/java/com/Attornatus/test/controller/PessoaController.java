package com.attornatus.test.controller;

import com.attornatus.test.entity.Pessoa;
import com.attornatus.test.service.EnderecoService;
import com.attornatus.test.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/pessoa", produces="application/json")
public class PessoaController {

    PessoaService pessoaService;
    @Autowired
    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }
    /*
    * Retorna a pessoa pelo id informado
    * */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPessoaById(@PathVariable Long id){
        return pessoaService.getPessoaById(id);
    }
    /*
     * Retorna a lista de pessoas cadastradas
     * */
    @GetMapping
    public ResponseEntity<Object> getPessoas( Pageable pageable){
        return pessoaService.getPessoas(pageable);

    }
    /*
     * Atualiza a pessoa informada
     * */
    @PutMapping
    public ResponseEntity<Object> updatePessoa(@RequestBody Pessoa pessoa){
        return pessoaService.updatePessoa(pessoa);
    }
    /*
     * Cria a pessoa informada
     * */
    @PostMapping
    public ResponseEntity<Object> createPessoa(@RequestBody Pessoa pessoa){
        return pessoaService.createPessoa(pessoa);
    }
    /*
     * remove a pessoa informada pelo id
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removePessoa(@PathVariable Long id){
        return pessoaService.removePessoa(id);
    }
}

package com.attornatus.test.controller;

import com.attornatus.test.entity.Pessoa;
import com.attornatus.test.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pessoa")
public class PessoaController {
    @Autowired
    PessoaService pessoaService;

    /*
    * Retorna a pessoa pelo id informado
    * */
    @GetMapping("/aaa/{id}")
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
    public ResponseEntity<Object> updatePessoa(Pessoa pessoa){
        return pessoaService.updatePessoa(pessoa);
    }
    /*
     * Cria a pessoa informada
     * */
    @PostMapping
    public ResponseEntity<Object> createPessoa(Pessoa pessoa){
        return pessoaService.createPessoa(pessoa);
    }
    /*
     * remove a pessoa informada pelo id
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> createPessoa(@PathVariable Long id){
        return pessoaService.removePessoa(id);
    }
}

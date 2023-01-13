package com.attornatus.test;

import com.attornatus.test.controller.PessoaController;
import com.attornatus.test.entity.Endereco;
import com.attornatus.test.entity.Pessoa;
import com.attornatus.test.repository.EnderecoRepository;
import com.attornatus.test.repository.PessoaRepository;
import com.attornatus.test.service.EnderecoService;
import com.attornatus.test.service.PessoaService;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;

import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@RunWith(MockitoJUnitRunner.class)
public class PessoaServiceTest extends TestCase {
    /*Correcao erro No tests found in PessoaServiceTest
     */
    @org.junit.Test
    public void test(){

    }
    @InjectMocks
    private PessoaService pessoaService;
    @InjectMocks
    @Spy
    private EnderecoService enderecoService;
    @Mock
    private PessoaRepository pessoaRepository;
    @Mock
    @Autowired
    private EnderecoRepository enderecoRepository;

    private Pessoa pessoaIn;
    private Pessoa pessoaOut;
    private  List<Pessoa> pessoaLista=new ArrayList<>();
    @Autowired
    private Endereco enderecoDefault;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @BeforeAll
    public  static void setupAll() throws ParseException {
        java.util.Date dt=new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2000");
        Pessoa pessoa=new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("pessoa teste");
        pessoa.setDataNascimento(new Date(dt.getTime()));
        Endereco endereco = new Endereco();
        endereco.setLogradouro("logradouro");
        endereco.setCep("75000000");
        endereco.setCidade("Anapolis");
        endereco.setNumero(0);
        endereco.setPessoa(pessoa);
        endereco.setEnderecoPrincipal(true);

    }

    @BeforeEach
    public void setupBeforeEach () throws  ParseException{
        java.util.Date dt=new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2000");
        pessoaIn=new Pessoa(null,"pessoa",new Date(dt.getTime()),new ArrayList<>());

        Endereco endereco = new Endereco(null,"logradouro","75000000",0,"anapolis",true,pessoaIn);
        Endereco endereco2 = new Endereco(null,"logradouro2","75000001",1,"anapolis",false,pessoaIn);
        pessoaIn.getEnderecos().add(endereco);
        pessoaIn.getEnderecos().add(endereco2);

        pessoaOut=new Pessoa(1L,"pessoa",new Date(dt.getTime()),new ArrayList<>());

        Endereco enderecoOut = new Endereco(1L,"logradouro","75000000",0,"anapolis",true,pessoaOut);
        Endereco endereco2Out = new Endereco(2L,"logradouro2","75000001",1,"anapolis",false,pessoaOut);
        pessoaOut.getEnderecos().add(enderecoOut);
        pessoaOut.getEnderecos().add(endereco2Out);

        pessoaLista.add(pessoaOut);
        pessoaLista.add(new Pessoa(3L,"pessoa3",new Date(dt.getTime()),new ArrayList<>()));
    }
    @Test
    public  void  testGetPessoaComTodosEnderecos() throws Exception{
        java.util.Date dt=new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2000");

        Pessoa pessoa1=new Pessoa(1L,"pessoa",new Date(dt.getTime()),new ArrayList<>());
        Pessoa pessoa2=new Pessoa(1L,"pessoa",new Date(dt.getTime()),new ArrayList<>());

        pessoa1.getEnderecos().add(new Endereco(1L,"logradouro","75000000",0,"anapolis",true,pessoaOut));
        pessoa2.getEnderecos().add(new Endereco(2L,"logradouro3","75000000",2,"anapolis",false,pessoaOut));
        pessoa2.getEnderecos().add(new Endereco(null,"logradouro","75000000",0,"anapolis",true,pessoaOut));
        pessoa1.getEnderecos().add(new Endereco(2L,"logradouro2","75000000",2,"anapolis",false,pessoaOut));

        Pessoa pessoaResult=assertDoesNotThrow(()->pessoaService.getPessoaComTodosEnderecos(pessoa1,pessoa2));
        Assert.assertEquals(3,pessoaResult.getEnderecos().size());
    }
    @Test
     public void getPessoaById(){
        Mockito.when(pessoaRepository.findById(1L)).thenReturn(Optional.ofNullable(pessoaOut));

        ResponseEntity<Object> re=pessoaService.getPessoaById(1L);
        ResponseEntity<Object> result= assertDoesNotThrow(()-> pessoaService.getPessoaById(1L));
        assertEquals(HttpStatus.OK,result.getStatusCode());
        Optional<Pessoa> pessoaResult= (Optional<Pessoa>) result.getBody();
        assertEquals(pessoaResult.get().getId(),pessoaOut.getId());
    }
    @Test
    public void getPessoas(){

        Pageable pageable=PageRequest.of(1, 10);
        Mockito.when(pessoaRepository.findAll()).thenReturn(pessoaLista);
        Page<Pessoa> pagePessoa=new PageImpl<>(pessoaLista.subList(0,2), pageable, pessoaLista.size());

        Mockito.when(pessoaRepository.findAll(pageable)).thenReturn(pagePessoa);
        ResponseEntity<Object> result= assertDoesNotThrow(()-> pessoaService.getPessoas(pageable));
        assertEquals(HttpStatus.OK,result.getStatusCode());
        Page<Pessoa> pessoaResult= (Page<Pessoa>) result.getBody();
        assertEquals(pessoaResult.get().count(),2);

    }
    @Test
    public void updatePessoa(){

        Mockito.when(pessoaRepository.findById(1L)).thenReturn(Optional.ofNullable(pessoaOut));
        pessoaOut.setNome("nome update");
        Mockito.when(pessoaRepository.save(pessoaIn)).thenReturn(pessoaOut);
        ResponseEntity<Object> result= assertDoesNotThrow(()-> pessoaService.updatePessoa(pessoaOut));
        assertEquals(HttpStatus.OK,result.getStatusCode());
        Pessoa pessoaResult= (Pessoa) result.getBody();
        assertEquals(new Long(1L),pessoaResult.getId());
        assertEquals(pessoaResult.getNome(),"nome update");
    }
    @Test
    public void createPessoa(){
        Mockito.when(pessoaRepository.save(pessoaIn)).thenReturn(pessoaOut);

        ResponseEntity<Object> result= assertDoesNotThrow(()-> pessoaService.createPessoa(pessoaIn));
        assertEquals(HttpStatus.CREATED,result.getStatusCode());
        Pessoa pessoaResult= (Pessoa) result.getBody();
    }
    @Test
    public void createPessoaComNomeIgual(){
        Mockito.when(pessoaRepository.findById(1L)).thenReturn(Optional.ofNullable(pessoaOut));
        Mockito.when(pessoaRepository.findByNome(pessoaIn.getNome())).thenReturn(Optional.ofNullable(pessoaOut));
        Mockito.when(pessoaRepository.save(pessoaIn)).thenReturn(pessoaOut);
        ResponseEntity<Object> result= assertDoesNotThrow(()-> pessoaService.createPessoa(pessoaOut));
        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());

    }
    @Test
    public void removePessoaPessoa(){
        ResponseEntity<Object> result= assertDoesNotThrow(()-> pessoaService.removePessoa(1L));
        assertEquals(HttpStatus.ACCEPTED,result.getStatusCode());

    }
}

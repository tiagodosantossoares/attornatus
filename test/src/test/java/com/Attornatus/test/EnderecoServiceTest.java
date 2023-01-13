package com.attornatus.test;

import com.attornatus.test.entity.Endereco;
import com.attornatus.test.entity.Pessoa;
import com.attornatus.test.repository.EnderecoRepository;
import com.attornatus.test.repository.PessoaRepository;
import com.attornatus.test.service.EnderecoService;
import com.attornatus.test.service.PessoaService;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@RunWith(MockitoJUnitRunner.class)
public class EnderecoServiceTest extends TestCase {
    /*Correcao erro No tests found in PessoaServiceTest
     */
    @org.junit.Test
    public void test(){

    }
    @InjectMocks
    private EnderecoService enderecoService;
    @Mock
    private PessoaRepository pessoaRepository;
    @Mock
    private EnderecoRepository enderecoRepository;

    private Endereco enderecoIn;
    private Endereco enderecoOut;

    private Pessoa pessoaIn;
    private Pessoa pessoaOut;
    private  List<Endereco> enderecoListIn=new ArrayList<>();
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setupBeforeEach () throws  ParseException{
        java.util.Date dt=new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2000");

        pessoaIn=new Pessoa(null,"pessoa",new Date(dt.getTime()),new ArrayList<>());
        pessoaOut=new Pessoa(1L,"pessoa",new Date(dt.getTime()),new ArrayList<>());
        enderecoIn = new Endereco(null,"logradouro","75000000",0,"anapolis",true,pessoaIn);
        enderecoOut = new Endereco(1L,"logradouro","75000000",1,"anapolis",true,pessoaOut);
        pessoaIn.getEnderecos().add(enderecoIn);
        pessoaOut.getEnderecos().add(enderecoOut);



        enderecoListIn.add(new Endereco(1L,"logradouro","75000000",0,"anapolis",true,pessoaOut));
        enderecoListIn.add(new Endereco(null,"logradouro","75000000",0,"anapolis",true,pessoaOut));
        enderecoListIn.add(new Endereco(2L,"logradouro2","75000000",2,"anapolis",false,pessoaOut));
        enderecoListIn.add(new Endereco(null,"logradouro2","75000000",2,"anapolis",false,pessoaOut));

    }
    @Test
    public void getEnderecoById(){
        Mockito.when(enderecoRepository.findById(1L)).thenReturn(Optional.ofNullable(enderecoOut));
        ResponseEntity<Object> re=enderecoService.getEnderecoById(1L);
        ResponseEntity<Object> result= assertDoesNotThrow(()-> enderecoService.getEnderecoById(1L));
        assertEquals(HttpStatus.OK,result.getStatusCode());
        Endereco pessoaResult= (Endereco) result.getBody();
        assertEquals(pessoaResult.getId(),pessoaOut.getId());
    }
    @Test
    public void getEnderecos(){

        Pageable pageable=PageRequest.of(1, 10);
        Mockito.when(enderecoRepository.findAll()).thenReturn(enderecoListIn);
        Page<Endereco> pageEndereco=new PageImpl<>(enderecoListIn.subList(0,2), pageable, enderecoListIn.size());

        Mockito.when(enderecoRepository.findAll(pageable)).thenReturn(pageEndereco);
        Page<Endereco> result= assertDoesNotThrow(()-> enderecoService.getEnderecos(pageable));

        assertEquals(result.get().count(),2);

    }
    @Test
    public void updateEndereco(){
        enderecoIn.setLogradouro("logradouro atualizado");
        enderecoOut.setLogradouro("logradouro atualizado");
        enderecoIn.setId(1L);
        Mockito.when(enderecoRepository.findById(1L)).thenReturn(Optional.ofNullable(enderecoOut));
        Mockito.when(pessoaRepository.findById(1L)).thenReturn(Optional.ofNullable(pessoaOut));
        Mockito.when(pessoaRepository.findByEnderecosId(1L)).thenReturn(Optional.ofNullable(pessoaOut));
        Mockito.when(enderecoRepository.findByPessoaIdAndLogradouroAndCepAndNumeroAndCidade(
                1L,enderecoIn.getLogradouro()
                ,enderecoIn.getCep()
                ,enderecoIn.getNumero()
                , enderecoIn.getCidade()
        )).thenReturn(new ArrayList<>());
        ResponseEntity<Object> result= assertDoesNotThrow(()-> enderecoService.updateEndereco(enderecoIn));
        assertEquals(HttpStatus.OK,result.getStatusCode());
        Endereco enderecoResult= (Endereco) result.getBody();
        assertEquals(new Long(1L),enderecoResult.getId());
        assertEquals(enderecoResult.getLogradouro(),"logradouro atualizado");
    }
    @Test
    public void createEndereco(){
        Mockito.when(enderecoRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        Mockito.when(pessoaRepository.findById(1L)).thenReturn(Optional.ofNullable(pessoaOut));
        Mockito.when(enderecoRepository.findByPessoaIdAndLogradouroAndCepAndNumeroAndCidade(
                1L,enderecoIn.getLogradouro()
                ,enderecoIn.getCep()
                ,enderecoIn.getNumero()
                , enderecoIn.getCidade()
        )).thenReturn(new ArrayList<>());

        ResponseEntity<Object> result= assertDoesNotThrow(()-> enderecoService.createEndereco(1L,enderecoIn));
        assertEquals(HttpStatus.CREATED,result.getStatusCode());
        Pessoa pessoaResult= (Pessoa) result.getBody();
    }
    @Test
    public void createEnderecoIgual(){
        Mockito.when(enderecoRepository.findById(1L)).thenReturn(Optional.ofNullable(enderecoOut));
        Mockito.when(pessoaRepository.findById(1L)).thenReturn(Optional.ofNullable(pessoaOut));
        Mockito.when(enderecoRepository.findByPessoaIdAndLogradouroAndCepAndNumeroAndCidade(
                1L,enderecoIn.getLogradouro()
                        ,enderecoIn.getCep()
                        ,enderecoIn.getNumero()
                        , enderecoIn.getCidade()
                )).thenReturn(enderecoListIn);
        Mockito.when(enderecoRepository.save(enderecoIn)).thenReturn(enderecoOut);
        ResponseEntity<Object> result= assertDoesNotThrow(()-> enderecoService.createEndereco(1L,enderecoIn));
        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());

    }
    @Test
    public void removePessoaEndereco(){
        ResponseEntity<Object> result= assertDoesNotThrow(()-> enderecoService.removeEndereco(1L));
        assertEquals(HttpStatus.ACCEPTED,result.getStatusCode());

    }
}

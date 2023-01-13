package com.attornatus.test.repository;
import com.attornatus.test.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa,Long>{
    Optional<Pessoa> findByNome(String nome);
    @Query(value = "SELECT * FROM pessoa p INNER JOIN endereco e WHERE e.pessoa_id = p.id and e.id=?;", nativeQuery = true)
    Optional<Pessoa> findByEnderecosId(Long id);
}
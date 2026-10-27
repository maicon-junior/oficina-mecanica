package br.edu.univille.poo.oficina.model.repository;

import br.edu.univille.poo.oficina.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCpf(String cpf);

    List<Cliente> findAllByOrderByNomeAsc();

    List<Cliente> findByNomeContainingIgnoreCaseOrCpfContainingOrderByNomeAsc(String nome, String cpf);
}

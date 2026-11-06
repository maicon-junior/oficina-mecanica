package br.edu.univille.poo.oficina.model.repository;

import br.edu.univille.poo.oficina.model.entity.Mecanico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {

    Optional<Mecanico> findByCpf(String cpf);

    List<Mecanico> findAllByOrderByNomeAsc();

    List<Mecanico> findByNomeContainingIgnoreCaseOrEspecialidadeContainingIgnoreCaseOrderByNomeAsc(String nome, String especialidade);
}

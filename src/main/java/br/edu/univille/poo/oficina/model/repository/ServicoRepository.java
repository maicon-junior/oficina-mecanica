package br.edu.univille.poo.oficina.model.repository;

import br.edu.univille.poo.oficina.model.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    Optional<Servico> findByDescricaoIgnoreCase(String descricao);

    List<Servico> findAllByOrderByDescricaoAsc();

    List<Servico> findByDescricaoContainingIgnoreCaseOrderByDescricaoAsc(String descricao);
}

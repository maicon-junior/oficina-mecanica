package br.edu.univille.poo.oficina.model.repository;

import br.edu.univille.poo.oficina.model.entity.ItemServico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemServicoRepository extends JpaRepository<ItemServico, Long> {

    boolean existsByServicoId(Long servicoId);
}

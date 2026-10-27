package br.edu.univille.poo.oficina.model.repository;

import br.edu.univille.poo.oficina.model.entity.OrdemServico;
import br.edu.univille.poo.oficina.model.entity.StatusOrdem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    boolean existsByVeiculoId(Long veiculoId);

    boolean existsByMecanicoId(Long mecanicoId);

    long countByStatus(StatusOrdem status);

    List<OrdemServico> findTop5ByOrderByDataAberturaDescIdDesc();

    @Query("""
            select o from OrdemServico o
            where (:status is null or o.status = :status)
              and upper(o.veiculo.placa) like upper(concat('%', :placa, '%'))
            order by o.dataAbertura desc, o.id desc
            """)
    List<OrdemServico> pesquisar(@Param("status") StatusOrdem status, @Param("placa") String placa);
}

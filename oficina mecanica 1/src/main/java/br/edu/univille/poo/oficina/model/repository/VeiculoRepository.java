package br.edu.univille.poo.oficina.model.repository;

import br.edu.univille.poo.oficina.model.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    Optional<Veiculo> findByPlaca(String placa);

    boolean existsByClienteId(Long clienteId);

    List<Veiculo> findAllByOrderByPlacaAsc();

    @Query("""
            select v from Veiculo v
            where upper(v.placa) like upper(concat('%', :busca, '%'))
               or upper(v.modelo) like upper(concat('%', :busca, '%'))
               or upper(v.cliente.nome) like upper(concat('%', :busca, '%'))
            order by v.placa
            """)
    List<Veiculo> pesquisar(@Param("busca") String busca);
}

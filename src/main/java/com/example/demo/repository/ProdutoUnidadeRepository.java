package com.example.demo.repository;

import com.example.demo.model.ProdutoUnidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoUnidadeRepository extends JpaRepository<ProdutoUnidade, Long> {
    List<ProdutoUnidade> findByUnidade_UnidadeIdAndDisponivelTrue(Long unidadeId);

    //boolean findByProdutoId(Long produtoId);

    boolean existsByProduto_ProdutoIdAndUnidade_UnidadeId(Long produtoId, Long unidadeId);

    Optional<ProdutoUnidade> findByProduto_ProdutoIdAndUnidade_UnidadeId(Long produtoId, Long unidadeId);
}

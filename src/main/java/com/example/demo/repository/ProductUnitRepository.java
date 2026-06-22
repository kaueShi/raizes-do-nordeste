package com.example.demo.repository;

import com.example.demo.model.Product;
import com.example.demo.model.ProductUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductUnitRepository extends JpaRepository<ProductUnit, Long> {
    List<ProductUnit> findByUnit_UnidadeIdAndDisponivelTrue(Long unidadeId);

    //boolean findByProdutoId(Long produtoId);

    boolean existsByProduct_ProdutoIdAndUnit_UnidadeId(Long produtoId, Long unidadeId);

    Optional<ProductUnit> findByProduct_ProdutoIdAndUnit_UnidadeId(Long produtoId, Long unidadeId);
}

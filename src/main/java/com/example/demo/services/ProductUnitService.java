package com.example.demo.services;

import com.example.demo.model.ProductUnit;
import com.example.demo.repository.ProductUnitRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductUnitService {

    private final ProductUnitRepository productUnitRepository;

    // Constructor injection is preferred over @Autowired
    public ProductUnitService(ProductUnitRepository productUnitRepository){
        this.productUnitRepository = productUnitRepository;
    }

    public boolean existsByProductAndUnit(Long produtoId, Long unidadeId){
        return productUnitRepository.existsByProduct_ProdutoIdAndUnit_UnidadeId(produtoId, unidadeId);
    }

    public boolean findByUnidadeId(Long unidadeId) {
        return productUnitRepository.findByUnidadeId(unidadeId);
    }

    public ProductUnit saveProductUnit(ProductUnit productUnit) {
        if(productUnitRepository.existsById(productUnit.getProdutoUnidadeId())){
            throw new RuntimeException("Product already exists");
        }
        return productUnitRepository.save(productUnit);
    }
    public void  delete(ProductUnit productUnit){
        productUnitRepository.delete(productUnit);

    }
/*
    public boolean findByProdutoId(Long produtoId) {
        return productUnitRepository.findByProdutoId(produtoId);
    }*/

    public Optional<ProductUnit> findById(Long id) {
        return productUnitRepository.findById(id);
    }

    public boolean findByProductAndUnit(Long aLong, Long unidadeId) {
        return productUnitRepository.findByUnit_UnidadeIdAndDisponivelTrue(unidadeId);
    }

    public List<ProductUnit> getAllProducts() {
        return productUnitRepository.findAll();
    }
}

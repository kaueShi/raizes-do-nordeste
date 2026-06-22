package com.example.demo.services;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.ProductUnit;
import com.example.demo.repository.ProductUnitRepository;
import org.springframework.stereotype.Service;
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

    public ProductUnit saveProductUnit(ProductUnit productUnit) {
        return productUnitRepository.save(productUnit);
    }

    public Optional<ProductUnit> findById(Long id) {
        return productUnitRepository.findById(id);
    }

    public List<ProductUnit> findDisponiveisPorUnidade(Long unidadeId) {
        return productUnitRepository.findByUnit_UnidadeIdAndDisponivelTrue(unidadeId);
    }


    public ProductUnit buscarVinculoValido(Long unidadeId, Long produtoUnidadeId) {
        ProductUnit productUnit = productUnitRepository.findById(produtoUnidadeId)
                .orElseThrow(() -> new ResourceNotFoundException("VINCULO_NAO_ENCONTRADO", "Vínculo não encontrado: " + produtoUnidadeId));

        if (!productUnit.getUnit().getUnidadeId().equals(unidadeId)) {
            throw new ResourceNotFoundException("VINCULO_NAO_ENCONTRADO", "Este vínculo não pertence à unidade informada.");
        }
        return productUnit;
    }

}

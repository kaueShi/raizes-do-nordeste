package com.example.demo.services;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductUnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductUnitRepository productUnitRepository;

    // Constructor injection is preferred over @Autowired
    public ProductService(ProductRepository productRepository, ProductUnitRepository productUnitRepository){
            this.productRepository = productRepository;
        this.productUnitRepository = productUnitRepository;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    public boolean existsByNome(String nome) {
        return productRepository.existsByNome(nome);
    }

    public Optional<Product> findById(Long produtoId) {
        return productRepository.findById(produtoId);
    }

    public Product getProductByNome(String nome) {
        return productRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Procut not found"));
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procut not found"));
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }

}

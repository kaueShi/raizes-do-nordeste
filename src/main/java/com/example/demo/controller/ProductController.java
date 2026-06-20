package com.example.demo.controller;

import com.example.demo.dtos.ProductCatalogResponseDto;
import com.example.demo.dtos.ProductDto;
import com.example.demo.model.Product;
import com.example.demo.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("produtos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid ProductDto data) {
        if(productService.existsByNome(data.nome())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Product already exists!");
        }
        var product = new Product();
        product.setNome(data.nome());
        product.setDescricao(data.descricao());
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(product));

        // checar duplicidade (existsByNome) -> 409 se já existir
        // mapear DTO -> Product, salvar
        // devolver 201 Created
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ProductDto data) {
            Optional<Product> productOptional = productService.findById(id);
            if (productOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
            }
            var product = new Product();
            product.setNome(data.nome());
            product.setDescricao(data.descricao());
            return ResponseEntity.status(HttpStatus.OK).body(productService.saveProduct(product));
        }

    @GetMapping
    public ResponseEntity<List<ProductCatalogResponseDto>> list() {
        var produtos = productService.getAllProducts().stream()
                .map(ProductCatalogResponseDto::new)
                .toList();
        return ResponseEntity.ok(produtos);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        productService.delete(productOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }


}

package com.example.demo.controller;

import com.example.demo.dtos.CardapioResponseDto;
import com.example.demo.dtos.ProductResponseDto;
import com.example.demo.dtos.ProductUnitDto;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.ProductUnit;
import com.example.demo.model.Unit;
import com.example.demo.services.ProductService;
import com.example.demo.services.ProductUnitService;
import com.example.demo.services.UnitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidades/{unidadeId}/produtos")
public class ProductUnitController {

    private final ProductUnitService productUnitService;
    private final ProductService productService;
    private final UnitService unitService;

    public ProductUnitController(ProductUnitService productUnitService, ProductService productService, UnitService unitService) {
        this.productUnitService = productUnitService;
        this.productService = productService;
        this.unitService = unitService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<?> vincular(@PathVariable Long unidadeId, @RequestBody @Valid ProductUnitDto data) {

        Unit unit = unitService.findById(unidadeId)
                .orElseThrow(() -> new ResourceNotFoundException("UNIDADE_NAO_ENCONTRADA", "Unidade não encontrada: " + unidadeId));

        Product product = productService.findById(data.produtoId())
                .orElseThrow(() -> new ResourceNotFoundException("PRODUTO_NAO_ENCONTRADO", "Produto não encontrado: " + data.produtoId()));
        if (productUnitService.existsByProductAndUnit(data.produtoId(), unidadeId)) {
            throw new BusinessRuleException("VINCULO_DUPLICADO", "Este produto já está vinculado a esta unidade.");
        }
        var productUnit = new ProductUnit();
        productUnit.setProduct(product);
        productUnit.setUnit(unit);
        productUnit.setPreco(data.preco());
        productUnit.setQuantidade(data.quantidade());
        productUnit.setDisponivel(data.disponivel());

        var saved = productUnitService.saveProductUnit(productUnit);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponseDto(saved));
    }
    // buscar Unit por unidadeId -> 404 se não achar
    // buscar Product por data.produtoId() -> 404 se não achar
    // checar existsByProduct_..AndUnit_.. -> 409 se já vinculado
    // criar ProductUnit, salvar
    // devolver 201 Created


    @PutMapping("/{produtoUnidadeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<?> atualizar(@PathVariable Long unidadeId, @PathVariable Long produtoUnidadeId,
                                       @RequestBody @Valid ProductUnitDto data) {
        ProductUnit productUnit = productUnitService.findById(produtoUnidadeId)
                .orElseThrow(() -> new ResourceNotFoundException("PRODUTO_NAO_ENCONTRADO", "Produto não encontrada na unidade: " + produtoUnidadeId));

        if (!productUnit.getUnit().getUnidadeId().equals(unidadeId)) {
            throw new BusinessRuleException("VINCULO_INEXISTENTE", "Este produto não está vinculado a esta unidade.");
        }
        productUnit.setPreco(data.preco());
        productUnit.setQuantidade(data.quantidade());
        productUnit.setDisponivel(data.disponivel());
        return ResponseEntity.status(HttpStatus.OK).body(productUnitService.saveProductUnit(productUnit));
        // buscar ProductUnit -> 404 se não achar (e validar que pertence à unidadeId do path)
        // atualizar preco/quantidade/disponivel
        // devolver 200 OK
    }


    @GetMapping
    public ResponseEntity<List<CardapioResponseDto>> cardapio(@PathVariable Long unidadeId) {
        if(!productUnitService.findByUnidadeId(unidadeId)){
            throw new BusinessRuleException("VINCULO_INEXISTENTE", "Este produto não está vinculado a esta unidade.");
        }
        // findByUnit_UnidadeIdAndDisponivelTrue(unidadeId)
        // mapear para DTO de resposta (nome, descricao, preco, quantidade, disponivel)
        // devolver 200 OK
        var cardapio = productUnitService.getAllProducts().stream()
                .map(CardapioResponseDto::new)
                .toList();
        return ResponseEntity.ok(cardapio);
    }

    @PatchMapping("/{produtoUnidadeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<?> desativar(@PathVariable Long unidadeId, @PathVariable Long produtoUnidadeId,
                                       @RequestBody @Valid ProductUnitDto data) {
        ProductUnit productUnit = productUnitService.findById(produtoUnidadeId)
            .orElseThrow(() -> new ResourceNotFoundException("PRODUTO_NAO_ENCONTRADO", "Produto não encontrada na unidade: " + produtoUnidadeId));

        if (!productUnit.getUnit().getUnidadeId().equals(unidadeId)) {
            throw new BusinessRuleException("VINCULO_INEXISTENTE", "Este produto não está vinculado a esta unidade.");
        }
        productUnit.setDisponivel(data.disponivel());
        return ResponseEntity.status(HttpStatus.OK).body(productUnitService.saveProductUnit(productUnit));

        /*
        *     @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        productService.delete(productOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }*/
    }



}
/*    public ResponseEntity<List<ProductCatalogResponseDto>> list() {
        var produtos = productService.getAllProducts().stream()
                .map(ProductCatalogResponseDto::new)
                .toList();
        return ResponseEntity.ok(produtos);*/






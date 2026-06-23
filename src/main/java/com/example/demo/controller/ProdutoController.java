package com.example.demo.controller;

import com.example.demo.dtos.ProdutoCatalogoResponseDto;
import com.example.demo.model.Produto;
import com.example.demo.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @PostMapping("/produtos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid ProdutoCatalogoResponseDto data) {
        if(produtoService.existsByNome(data.nome())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Produto already exists!");
        }
        var product = new Produto();
        product.setNome(data.nome());
        product.setDescricao(data.descricao());
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.saveProduct(product));

        // checar duplicidade (existsByNome) -> 409 se já existir
        // mapear DTO -> Produto, salvar
        // devolver 201 Created
    }

    @PutMapping("/produtos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoCatalogoResponseDto data) {
            Optional<Produto> productOptional = produtoService.findById(id);
            if (productOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto not found.");
            }
            Produto produto = productOptional.get();
            produto.setNome(data.nome());
            produto.setDescricao(data.descricao());
            return ResponseEntity.status(HttpStatus.OK).body(produtoService.saveProduct(produto));
        }

    @GetMapping
    public ResponseEntity<List<ProdutoCatalogoResponseDto>> list() {
        var produtos = produtoService.getAllProducts().stream()
                .map(ProdutoCatalogoResponseDto::new)
                .toList();
        return ResponseEntity.ok(produtos);
    }

    @DeleteMapping("/produtos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Produto> productOptional = produtoService.findById(id);
        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto not found.");
        }
        produtoService.delete(productOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deleted successfully.");
    }


}

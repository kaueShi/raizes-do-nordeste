package com.example.demo.controller;

import com.example.demo.dtos.produto.ProdutoCatalogoResponseDto;
import com.example.demo.dtos.produto.ProdutoDto;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.Produto;
import com.example.demo.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Produtos", description = "Catálogo de produtos da rede - operações restritas ao ADMIN")
@RestController
@RequestMapping("produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @Operation(summary = "Lista todos os produtos do catálogo")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ProdutoCatalogoResponseDto>> listar() {
        var produtos = produtoService.getAllProducts().stream()
                .map(ProdutoCatalogoResponseDto::new)
                .toList();
        return ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Cadastra novo produto no catálogo - somente ADMIN")
    @ApiResponse(responseCode = "201", description = "Produto criado")
    @ApiResponse(responseCode = "409", description = "Produto com mesmo nome já existe")
    @ApiResponse(responseCode = "422", description = "Campos inválidos")
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProdutoCatalogoResponseDto> criar(@RequestBody @Valid ProdutoDto data) {
        if(produtoService.existsByNome(data.nome())){
            throw new BusinessRuleException("PRODUTO_JA_EXISTE", "Produto já existe");
        }
        var produto = new Produto();
        produto.setNome(data.nome());
        produto.setDescricao(data.descricao());
        var saved = produtoService.saveProduto(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProdutoCatalogoResponseDto(saved));
    }

    @Operation(summary = "Atualiza nome/descrição de um produto - somente ADMIN")
    @ApiResponse(responseCode = "200", description = "Produto atualizado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProdutoCatalogoResponseDto> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoDto data) {
            Optional<Produto> produtoOptional = produtoService.findById(id);
            if (produtoOptional.isEmpty()) {
                throw new ResourceNotFoundException("PRODUTO_NAO_ENCONTRADO", "Produto não encontrado");
            }

            Produto produto = produtoOptional.get();
            produto.setNome(data.nome());
            produto.setDescricao(data.descricao());
            var saved = produtoService.saveProduto(produto);
            return ResponseEntity.status(HttpStatus.OK).body(new ProdutoCatalogoResponseDto(saved));
    }

    @Operation(summary = "Remove produto do catálogo - somente ADMIN")
    @ApiResponse(responseCode = "204", description = "Produto removido")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Produto> produtoOptional = produtoService.findById(id);
        if (!produtoOptional.isPresent()) {
            throw new ResourceNotFoundException("PRODUTO_NAO_ENCONTRADO", "Produto não encontrado");
        }
        produtoService.delete(produtoOptional.get());
        return ResponseEntity.noContent().build();
    }
}

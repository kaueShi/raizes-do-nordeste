package com.example.demo.controller;

import com.example.demo.dtos.produto.CardapioResponseDto;
import com.example.demo.dtos.produto.ProdutoResponseDto;
import com.example.demo.dtos.produto.ProdutoUnidadeDto;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.Produto;
import com.example.demo.model.ProdutoUnidade;
import com.example.demo.model.Unidade;
import com.example.demo.services.ProdutoService;
import com.example.demo.services.ProdutoUnidadeService;
import com.example.demo.services.UnidadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidades/{unidadeId}/produtos")
public class ProdutoUnidadeController {

    private final ProdutoUnidadeService produtoUnidadeService;
    private final ProdutoService produtoService;
    private final UnidadeService unidadeService;

    public ProdutoUnidadeController(ProdutoUnidadeService produtoUnidadeService, ProdutoService produtoService, UnidadeService unidadeService) {
        this.produtoUnidadeService = produtoUnidadeService;
        this.produtoService = produtoService;
        this.unidadeService = unidadeService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<?> vincular(@PathVariable Long unidadeId, @RequestBody @Valid ProdutoUnidadeDto data) {

        Unidade unidade = unidadeService.findById(unidadeId)
                .orElseThrow(() -> new ResourceNotFoundException("UNIDADE_NAO_ENCONTRADA", "Unidade não encontrada: " + unidadeId));

        Produto produto = produtoService.findById(data.produtoId())
                .orElseThrow(() -> new ResourceNotFoundException("PRODUTO_NAO_ENCONTRADO", "Produto não encontrado: " + data.produtoId()));
        if (produtoUnidadeService.existsByProdutoAndUnidade(data.produtoId(), unidadeId)) {
            throw new BusinessRuleException("VINCULO_DUPLICADO", "Este produto já está vinculado a esta unidade.");
        }
        var productUnit = new ProdutoUnidade();
        productUnit.setProduto(produto);
        productUnit.setUnidade(unidade);
        productUnit.setPreco(data.preco());
        productUnit.setQuantidade(data.quantidade());
        productUnit.setDisponivel(data.disponivel());

        var saved = produtoUnidadeService.saveProdutoUnidade(productUnit);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProdutoResponseDto(saved));
    }

    @PutMapping("/{produtoUnidadeId}/preco/estoque")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<?> atualizar(@PathVariable Long unidadeId, @PathVariable Long produtoUnidadeId,
                                       @RequestBody @Valid ProdutoUnidadeDto data) {
        ProdutoUnidade produtoUnidade = produtoUnidadeService.buscarVinculoValido(unidadeId, produtoUnidadeId);
        produtoUnidade.setPreco(data.preco());
        produtoUnidade.setQuantidade(data.quantidade());
        produtoUnidade.setDisponivel(data.disponivel());
        return ResponseEntity.status(HttpStatus.OK).body(produtoUnidadeService.saveProdutoUnidade(produtoUnidade));
    }


    @GetMapping
    public ResponseEntity<List<CardapioResponseDto>> cardapio(@PathVariable Long unidadeId) {
        var cardapio = produtoUnidadeService.findDisponiveisPorUnidade(unidadeId).stream()
                .map(CardapioResponseDto::new)
                .toList();
        return ResponseEntity.ok(cardapio);
    }

    @PatchMapping("/{produtoUnidadeId}/desativar")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<?> desativar(@PathVariable Long unidadeId, @PathVariable Long produtoUnidadeId) {
        ProdutoUnidade produtoUnidade = produtoUnidadeService.buscarVinculoValido(unidadeId, produtoUnidadeId);
        produtoUnidade.setDisponivel(false);
        return ResponseEntity.status(HttpStatus.OK).body(produtoUnidadeService.saveProdutoUnidade(produtoUnidade));
    }

    @PatchMapping("/{produtoUnidadeId}/reativar")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<?> reativar(@PathVariable Long unidadeId, @PathVariable Long produtoUnidadeId) {
        ProdutoUnidade produtoUnidade = produtoUnidadeService.buscarVinculoValido(unidadeId, produtoUnidadeId);
        produtoUnidade.setDisponivel(true);
        var saved = produtoUnidadeService.saveProdutoUnidade(produtoUnidade);
        return ResponseEntity.ok(new ProdutoResponseDto(saved));
    }

}






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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cardápio por Unidade", description = "Vínculo de produtos às unidades e gestão de disponibilidade")
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

    @Operation(summary = "Lista cardápio disponível da unidade - público")
    @ApiResponse(responseCode = "200", description = "Cardápio retornado")
    @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
    @GetMapping
    public ResponseEntity<List<CardapioResponseDto>> cardapio(@PathVariable Long unidadeId) {
        var cardapio = produtoUnidadeService.findDisponiveisPorUnidade(unidadeId).stream()
                .map(CardapioResponseDto::new)
                .toList();
        return ResponseEntity.ok(cardapio);
    }

    @Operation(summary = "Vincula produto ao cardápio da unidade - ADMIN ou FUNCIONARIO")
    @ApiResponse(responseCode = "201", description = "Produto vinculado")
    @ApiResponse(responseCode = "404", description = "Produto ou unidade não encontrado")
    @ApiResponse(responseCode = "409", description = "Produto já vinculado a esta unidade")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<ProdutoResponseDto> vincular(@PathVariable Long unidadeId, @RequestBody @Valid ProdutoUnidadeDto data) {

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

    @Operation(summary = "Atualizar preço, estoque e disponibilidade - ADMIN ou FUNCIONARIO")
    @ApiResponse(responseCode = "200", description = "Vínculo atualizado")
    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    @PutMapping("/{produtoUnidadeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<ProdutoResponseDto> atualizar(@PathVariable Long unidadeId, @PathVariable Long produtoUnidadeId,
                                       @RequestBody @Valid ProdutoUnidadeDto data) {
        ProdutoUnidade produtoUnidade = produtoUnidadeService.buscarVinculoValido(unidadeId, produtoUnidadeId);
        produtoUnidade.setPreco(data.preco());
        produtoUnidade.setQuantidade(data.quantidade());
        produtoUnidade.setDisponivel(data.disponivel());
        var saved = produtoUnidadeService.saveProdutoUnidade(produtoUnidade);
        return ResponseEntity.status(HttpStatus.OK).body(new ProdutoResponseDto(saved));
    }

    @Operation(summary = "Desativar produto no cardápio da unidade - ADMIN ou FUNCIONARIO")
    @ApiResponse(responseCode = "200", description = "Produto ativado")
    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    @PatchMapping("/{produtoUnidadeId}/desativar")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<ProdutoResponseDto> desativar(@PathVariable Long unidadeId, @PathVariable Long produtoUnidadeId) {
        ProdutoUnidade produtoUnidade = produtoUnidadeService.buscarVinculoValido(unidadeId, produtoUnidadeId);
        produtoUnidade.setDisponivel(false);
        var saved = produtoUnidadeService.saveProdutoUnidade(produtoUnidade);
        return ResponseEntity.ok(new ProdutoResponseDto(saved));
    }

    @Operation(summary = "Reativa produto no cardápio da unidade - ADMIN ou FUNCIONARIO")
    @ApiResponse(responseCode = "200", description = "Produto reativado")
    @PatchMapping("/{produtoUnidadeId}/reativar")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<ProdutoResponseDto> reativar(@PathVariable Long unidadeId, @PathVariable Long produtoUnidadeId) {
        ProdutoUnidade produtoUnidade = produtoUnidadeService.buscarVinculoValido(unidadeId, produtoUnidadeId);
        produtoUnidade.setDisponivel(true);
        var saved = produtoUnidadeService.saveProdutoUnidade(produtoUnidade);
        return ResponseEntity.ok(new ProdutoResponseDto(saved));
    }
}






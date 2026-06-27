package com.example.demo.controller;

import com.example.demo.dtos.pagamento.PagamentoDto;
import com.example.demo.dtos.pagamento.PagamentoResponseDto;
import com.example.demo.model.Pagamento;
import com.example.demo.model.Usuario;
import com.example.demo.services.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pagamento", description = "Processamento de pagamento mock - sem integração real")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("pedidos/{pedidoId}/pagamento")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService){
        this.pagamentoService = pagamentoService;
    }

    @Operation(summary = "Processa pagamento mock do pedido - CLIENTE ou FUNCIONARIO",
            description = "Use simularFalha=true para simular recusa. Quando aprovado avança, decrementa estoque e avanaça pedido para EM_PREPARO.")
    @ApiResponse(responseCode = "201", description = "Pagamento processado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @ApiResponse(responseCode = "409", description = "Pagamento já processado para este pedido")
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'FUNCIONARIO')")
    public ResponseEntity<PagamentoResponseDto> processar(@PathVariable Long pedidoId,
                                       @AuthenticationPrincipal Usuario usuarioLogado,
                                       @RequestBody @Valid PagamentoDto data){
        Pagamento pagamento = pagamentoService.processarPagamento(pedidoId, usuarioLogado, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PagamentoResponseDto(pagamento));
    }
}

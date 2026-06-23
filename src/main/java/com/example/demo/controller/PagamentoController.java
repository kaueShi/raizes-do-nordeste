package com.example.demo.controller;

import com.example.demo.dtos.PagamentoDto;
import com.example.demo.dtos.PagamentoResponseDto;
import com.example.demo.model.Pagamento;
import com.example.demo.model.Usuario;
import com.example.demo.services.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pedidos/{pedidoId}/pagamento")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService){
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'FUNCIONARIO')")
    public ResponseEntity<?> processar(@PathVariable Long pedidoId,
                                       @AuthenticationPrincipal Usuario usuarioLogado,
                                       @RequestBody @Valid PagamentoDto data){
        Pagamento pagamento = pagamentoService.processarPagamento(pedidoId, usuarioLogado, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PagamentoResponseDto(pagamento));
    }
}

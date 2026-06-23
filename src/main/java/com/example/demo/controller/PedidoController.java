package com.example.demo.controller;

import com.example.demo.dtos.pedido.PedidoDto;
import com.example.demo.dtos.pedido.PedidoResponseDto;
import com.example.demo.dtos.pedido.StatusUpdateDto;
import com.example.demo.enums.CanalPedido;
import com.example.demo.model.Pedido;
import com.example.demo.model.Usuario;
import com.example.demo.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'FUNCIONARIO')")
    public ResponseEntity<?> criar(@AuthenticationPrincipal Usuario usuarioLogado, @RequestBody @Valid PedidoDto data) {
        Pedido pedido = pedidoService.criarPedido(usuarioLogado, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PedidoResponseDto(pedido));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<?> listar(@RequestParam(required = false) CanalPedido canalPedido) {
        var pedidos = pedidoService.listar(canalPedido).stream().map(PedidoResponseDto::new).toList();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id,
                                    @AuthenticationPrincipal Usuario usuarioAutenticado) {
        Pedido pedido = pedidoService.buscarPorId(id, usuarioAutenticado);
        return ResponseEntity.ok(new PedidoResponseDto(pedido));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody @Valid StatusUpdateDto data) {
        Pedido pedido = pedidoService.atualizarStatus(id, data.status());
        return ResponseEntity.ok(new PedidoResponseDto(pedido));
    }
}

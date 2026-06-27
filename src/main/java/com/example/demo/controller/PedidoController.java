package com.example.demo.controller;

import com.example.demo.dtos.pedido.PedidoDto;
import com.example.demo.dtos.pedido.PedidoResponseDto;
import com.example.demo.dtos.pedido.StatusUpdateDto;
import com.example.demo.enums.CanalPedido;
import com.example.demo.model.Pedido;
import com.example.demo.model.Usuario;
import com.example.demo.services.PedidoService;
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

import java.util.List;

@Tag(name = "Pedidos", description = "Criação e acompanhamento de pedidos")
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Cria um novo pedido - CLIENTE ou FUNCIONARIO",
            description = "Valida estoque sem decrementar. Desconto ocorre apenas após confirmação do pagamento")
    @ApiResponse(responseCode = "201", description = "Pedido criado com status AGUARDANDO_PAGAMENTO")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "404", description = "Unidade ou produto não encontrado")
    @ApiResponse(responseCode = "409", description = "Estoque insuficiente")
    @ApiResponse(responseCode = "422", description = "Campos inválidos ou canalPedido ausente")
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'FUNCIONARIO')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PedidoResponseDto> criar(@AuthenticationPrincipal Usuario usuarioLogado, @RequestBody @Valid PedidoDto data) {
        Pedido pedido = pedidoService.criarPedido(usuarioLogado, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PedidoResponseDto(pedido));
    }

    @Operation(summary = "Lista pedidos com filtro opcional por canal - ADMIN ou FUNCIONARIO",
            description = "Use ?canalPedido=APP para filtrar por canal de origem.")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<PedidoResponseDto>> listar(@RequestParam(required = false) CanalPedido canalPedido) {
        var pedidos = pedidoService.listar(canalPedido).stream().map(PedidoResponseDto::new).toList();
        return ResponseEntity.ok(pedidos);
    }

    @Operation(summary = "Buscar pedido por ID",
            description = "CLIENTE só pode ver os próprios pedidos. ADMIN e FUNCIONARIO veem qualquer pedido.")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "403", description = "Pedido de outro cliente")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PedidoResponseDto> buscar(@PathVariable Long id,
                                    @AuthenticationPrincipal Usuario usuarioAutenticado) {
        Pedido pedido = pedidoService.buscarPorId(id, usuarioAutenticado);
        return ResponseEntity.ok(new PedidoResponseDto(pedido));
    }

    @Operation(summary = "Atualiza status do pedido - ADMIN ou FUNCIONARIO",
            description = "Respeita a máquina de estados: AGUARDANDO_PAGAMENTO -> EM_PREPARO -> PRONTO -> ENTREGUE")
    @ApiResponse(responseCode = "200", description = "Status atualizado")
    @ApiResponse(responseCode = "409", description = "Transição de status inválida")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PedidoResponseDto> atualizarStatus(@PathVariable Long id, @RequestBody @Valid StatusUpdateDto data) {
        Pedido pedido = pedidoService.atualizarStatus(id, data.status());
        return ResponseEntity.ok(new PedidoResponseDto(pedido));
    }
}

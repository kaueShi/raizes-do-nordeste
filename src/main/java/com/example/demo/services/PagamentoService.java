package com.example.demo.services;

import com.example.demo.dtos.PagamentoDto;
import com.example.demo.enums.StatusPagamento;
import com.example.demo.enums.StatusPedido;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.model.Pagamento;
import com.example.demo.model.Pedido;
import com.example.demo.model.UserModel;
import com.example.demo.repository.PagamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PedidoService pedidoService;

    public PagamentoService(PagamentoRepository pagamentoRepository, PedidoService pedidoService) {
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoService = pedidoService;
    }

    @Transactional
    public Pagamento processarPagamento(Long pedidoId, UserModel usuarioLogado, PagamentoDto data) {
        Pedido pedido = pedidoService.buscarPorId(pedidoId, usuarioLogado); // reaproveita a checagem de dono que já existe
        if(pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO){
            throw new BusinessRuleException("PAGAMENTO_JA_PROCESSADO", "Pagamento já processado");
        }
        // TODO: validar pedido.getStatus() == StatusPedido.AGUARDANDO_PAGAMENTO
        //       -> se não for, throw BusinessRuleException("PAGAMENTO_JA_PROCESSADO", ...) (409)

        StatusPagamento statusPagamento = Boolean.TRUE.equals(data.simularFalha())
                ? StatusPagamento.RECUSADO
                : StatusPagamento.APROVADO;

        var pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setValor(pedido.getTotal());
        pagamento.setFormaPagamento(data.formaPagamento());
        pagamento.setStatus(statusPagamento);
        pagamento.setTransacaoId(UUID.randomUUID().toString());

        if(statusPagamento == StatusPagamento.APROVADO){
            pedidoService.confirmaPagamento(pedido);
        } else {
            pedidoService.recusarPagamento(pedido);

        }
        // TODO: if (statusPagamento == StatusPagamento.APROVADO) pedidoService.confirmarPagamento(pedido);
        //       else pedidoService.recusarPagamento(pedido);

        return pagamentoRepository.save(pagamento);
    }
}

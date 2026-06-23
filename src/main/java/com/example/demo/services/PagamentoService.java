package com.example.demo.services;

import com.example.demo.dtos.pagamento.PagamentoDto;
import com.example.demo.enums.StatusPagamento;
import com.example.demo.enums.StatusPedido;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.model.Pagamento;
import com.example.demo.model.Pedido;
import com.example.demo.model.Usuario;
import com.example.demo.repository.PagamentoRepository;
import jakarta.transaction.Transactional;
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
    public Pagamento processarPagamento(Long pedidoId, Usuario usuarioLogado, PagamentoDto data) {
        Pedido pedido = pedidoService.buscarPorId(pedidoId, usuarioLogado); // reaproveita a checagem de dono que já existe
        if(pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO){
            throw new BusinessRuleException("PAGAMENTO_JA_PROCESSADO", "Pagamento já processado");
        }

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

        return pagamentoRepository.save(pagamento);
    }
}

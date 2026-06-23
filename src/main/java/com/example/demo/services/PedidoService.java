package com.example.demo.services;

import com.example.demo.dtos.pedido.ItemPedidoDto;
import com.example.demo.dtos.pedido.PedidoDto;
import com.example.demo.enums.CanalPedido;
import com.example.demo.enums.StatusPedido;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProdutoUnidadeRepository;
import com.example.demo.repository.UnidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UnidadeRepository unidadeRepository;
    private final ProdutoUnidadeRepository produtoUnidadeRepository;

    public PedidoService(PedidoRepository pedidoRepository, UnidadeRepository unidadeRepository, ProdutoUnidadeRepository produtoUnidadeRepository){
        this.pedidoRepository = pedidoRepository;
        this.unidadeRepository = unidadeRepository;
        this.produtoUnidadeRepository = produtoUnidadeRepository;
    }

    @Transactional
    public Pedido criarPedido(Usuario cliente, PedidoDto data) {
        Unidade unidade = unidadeRepository.findById(data.unidadeId())
                .orElseThrow(() -> new ResourceNotFoundException("UNIDADE_NAO_ENCONTRADA", "Unidade não encontrada" + data.unidadeId()));

        var pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setUnidadePedido(unidade);
        pedido.setCanalPedido(data.canalPedido());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoDto itemDto : data.itens()) {
            ProdutoUnidade produtoUnidade = produtoUnidadeRepository.findByProduto_ProdutoIdAndUnidade_UnidadeId(itemDto.produtoId(), data.unidadeId())
                    .orElseThrow(() -> new ResourceNotFoundException("PRODUTO_NAO_ENCONTRADO", "Produto não encontrado" + data.itens()));
            if (produtoUnidade.getQuantidade() < itemDto.quantidade()) {
                throw new BusinessRuleException("ESTOQUE_INSUFICIENTE",
                        "Estoque insuficiente para o produto " + itemDto.produtoId() + ". Disponível: " + produtoUnidade.getQuantidade());
            }
            var item = new ItemPedido();
            item.setPedido(pedido); // crucial: sem isso o FK pedido_id fica null no banco
            item.setProdutoUnidade(produtoUnidade);
            item.setQuantidade(itemDto.quantidade());
            item.setPrecoUnitario(produtoUnidade.getPreco()); // captura o preço AGORA

            itens.add(item);
            total = total.add(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }

        pedido.setItens(itens);
        pedido.setTotal(total);

        return pedidoRepository.save(pedido); // cascade salva os itens junto, ver nota abaixo
    }
    public Pedido atualizarStatus(Long pedidoId, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("PEDIDO_NAO_ENCONTRADO", "Pedido não encontrado: " + pedidoId));
        if (!pedido.getStatus().podeMudarPara(novoStatus)) {
            throw new BusinessRuleException("TRANSICAO_INVALIDA",
                    String.format("Transição inválida: %s → %s", pedido.getStatus(), novoStatus));
        }
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listar(CanalPedido canalPedido) {
        if (canalPedido != null) {
            return pedidoRepository.findByCanalPedido(canalPedido);
        }
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id, Usuario usuarioLogado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("PEDIDO_NAO_ENCONTRADO", "Pedido não encontrado: " + id));
        boolean ehAdminOuFuncionario = usuarioLogado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") ||
                        a.getAuthority().equals("ROLE_FUNCIONARIO"));

        // Se NÃO for admin/funcionario E NÃO for o dono do pedido -> Acesso Negado
        if (!ehAdminOuFuncionario && !pedido.getCliente().getUsuarioId().equals(usuarioLogado.getUsuarioId())) {
            throw new BusinessRuleException("ACESSO_NEGADO", "Você não tem permissão para visualizar este pedido.");
        }
        return pedido;
    }

    @Transactional
    public void confirmaPagamento(Pedido pedido){
        for (ItemPedido item : pedido.getItens()){
            ProdutoUnidade pu = item.getProdutoUnidade();
            pu.setQuantidade(pu.getQuantidade() - item.getQuantidade());
            produtoUnidadeRepository.save(pu);
        }
        pedido.setStatus(StatusPedido.EM_PREPARO);
        pedidoRepository.save(pedido);
    }

    public void recusarPagamento(Pedido pedido) {
        pedido.setStatus(StatusPedido.PAGAMENTO_RECUSADO); // ou o nome corrigido, se você já ajustou o typo
        pedidoRepository.save(pedido);
    }
}

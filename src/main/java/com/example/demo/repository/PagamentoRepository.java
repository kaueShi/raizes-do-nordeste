package com.example.demo.repository;

import com.example.demo.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    Optional<Pagamento> findByPedido_PedidoId(Long pedidoId);

}

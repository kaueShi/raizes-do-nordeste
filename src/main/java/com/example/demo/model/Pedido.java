package com.example.demo.model;

import com.example.demo.enums.CanalPedido;
import com.example.demo.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pedidoId;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private UserModel cliente;

    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unit unidadePedido;

    @Enumerated(EnumType.STRING)
    private CanalPedido canalPedido;

    @CreationTimestamp
    private LocalDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private BigDecimal total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();

}

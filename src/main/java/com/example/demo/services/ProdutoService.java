package com.example.demo.services;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.Produto;
import com.example.demo.repository.ProdutoRepository;
import com.example.demo.repository.ProdutoUnidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoUnidadeRepository produtoUnidadeRepository;

    // Constructor injection is preferred over @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, ProdutoUnidadeRepository produtoUnidadeRepository){
            this.produtoRepository = produtoRepository;
        this.produtoUnidadeRepository = produtoUnidadeRepository;
    }

    public List<Produto> getAllProducts(){
        return produtoRepository.findAll();
    }

    public Produto saveProduto(Produto produto) {
        return produtoRepository.save(produto);
    }
    public boolean existsByNome(String nome) {
        return produtoRepository.existsByNome(nome);
    }

    public Optional<Produto> findById(Long produtoId) {
        return produtoRepository.findById(produtoId);
    }

    public Produto getProdutoByNome(String nome) {
        return produtoRepository.findByNome(nome)
                .orElseThrow(() -> new ResourceNotFoundException("PRODUTO_NAO_ENCONTRADO",
                        "Produto não encontrado: " + nome));
    }

    public Produto getProdutoById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PRODUTO_NAO_ENCONTRADO",
                        "Produto não encontrado: " + id));
    }

    public void delete(Produto produto) {
        produtoRepository.delete(produto);
    }

}

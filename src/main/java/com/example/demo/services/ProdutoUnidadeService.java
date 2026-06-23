package com.example.demo.services;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.ProdutoUnidade;
import com.example.demo.repository.ProdutoUnidadeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoUnidadeService {

    private final ProdutoUnidadeRepository produtoUnidadeRepository;

    // Constructor injection is preferred over @Autowired
    public ProdutoUnidadeService(ProdutoUnidadeRepository produtoUnidadeRepository){
        this.produtoUnidadeRepository = produtoUnidadeRepository;
    }

    public boolean existsByProdutoAndUnidade(Long produtoId, Long unidadeId){
        return produtoUnidadeRepository.existsByProduto_ProdutoIdAndUnidade_UnidadeId(produtoId, unidadeId);
    }

    public ProdutoUnidade saveProdutoUnidade(ProdutoUnidade produtoUnidade) {
        return produtoUnidadeRepository.save(produtoUnidade);
    }

    public Optional<ProdutoUnidade> findById(Long id) {
        return produtoUnidadeRepository.findById(id);
    }

    public List<ProdutoUnidade> findDisponiveisPorUnidade(Long unidadeId) {
        return produtoUnidadeRepository.findByUnidade_UnidadeIdAndDisponivelTrue(unidadeId);
    }


    public ProdutoUnidade buscarVinculoValido(Long unidadeId, Long produtoUnidadeId) {
        ProdutoUnidade produtoUnidade = produtoUnidadeRepository.findById(produtoUnidadeId)
                .orElseThrow(() -> new ResourceNotFoundException("VINCULO_NAO_ENCONTRADO", "Vínculo não encontrado: " + produtoUnidadeId));

        if (!produtoUnidade.getUnidade().getUnidadeId().equals(unidadeId)) {
            throw new ResourceNotFoundException("VINCULO_NAO_ENCONTRADO", "Este vínculo não pertence à unidade informada.");
        }
        return produtoUnidade;
    }

}

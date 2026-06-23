package com.example.demo.services;

import com.example.demo.model.Unidade;
import com.example.demo.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;

    // Constructor injection is preferred over @Autowired
    public UnidadeService(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    public List<Unidade> getAllUnits() {
        return unidadeRepository.findAll();
    }

    public Unidade saveProduct(Unidade unidade) {
        return unidadeRepository.save(unidade);
    }

    public Optional<Unidade> findById(Long unidadeId) {
        return unidadeRepository.findById(unidadeId);
    }
}
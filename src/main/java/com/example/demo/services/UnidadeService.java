package com.example.demo.services;

import com.example.demo.model.Unidade;
import com.example.demo.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;

    public UnidadeService(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    public List<Unidade> getAllUnidades() {
        return unidadeRepository.findAll();
    }

    public boolean existsByNome(String nome) {
        return unidadeRepository.existsByNome(nome);
    }

    public Unidade saveUnidade(Unidade unidade) {
        return unidadeRepository.save(unidade);
    }

    public Optional<Unidade> findById(Long unidadeId) {
        return unidadeRepository.findById(unidadeId);
    }
}
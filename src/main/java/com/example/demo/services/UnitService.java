package com.example.demo.services;

import com.example.demo.model.Unit;
import com.example.demo.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    // Constructor injection is preferred over @Autowired
    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    public Unit saveProduct(Unit unit) {
        return unitRepository.save(unit);
    }

    public Optional<Unit> findById(Long unidadeId) {
        return unitRepository.findById(unidadeId);
    }
}
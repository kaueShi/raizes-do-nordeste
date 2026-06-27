package com.example.demo.controller;

import com.example.demo.dtos.unidade.UnidadeDto;
import com.example.demo.dtos.unidade.UnidadeResponseDto;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.model.Unidade;
import com.example.demo.services.UnidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Unidades", description = "Gestão de unidades da rede")
@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    private final UnidadeService unidadeService;

    public UnidadeController(UnidadeService unidadeService) {
        this.unidadeService = unidadeService;
    }

    @Operation(summary = "Lista todas as unidades da rede")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<UnidadeResponseDto>> listar() {
        var unidades = unidadeService.getAllUnidades().stream()
                .map(UnidadeResponseDto::new)
                .toList();
        return ResponseEntity.ok(unidades);
    }

    @Operation(summary = "Cria uma nova unidade - somente ADMIN")
    @ApiResponse(responseCode = "201", description = "Unidade criada com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    @ApiResponse(responseCode = "403", description = "Sem permissão")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UnidadeResponseDto> criar(@RequestBody @Valid UnidadeDto data) {
        if(unidadeService.existsByNome(data.nome())){
            throw new BusinessRuleException("UNIDADE_DUPLICADA", "Já existe uma unidade com este nome.");
        }
        var unidade = new Unidade();
        unidade.setNome(data.nome());
        unidade.setCidade(data.cidade());
        unidade.setUf(data.uf());
        unidade.setCozinhaCompleta(data.cozinhaCompleta());
        var saved = unidadeService.saveUnidade(unidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UnidadeResponseDto(saved));
    }
}

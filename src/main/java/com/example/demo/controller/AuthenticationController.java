package com.example.demo.controller;

import com.example.demo.config.security.TokenService;
import com.example.demo.dtos.auth.AuthenticationDto;
import com.example.demo.dtos.auth.LoginResponseDto;
import com.example.demo.dtos.auth.RegisterDto;
import com.example.demo.dtos.auth.UsuarioResponseDto;
import com.example.demo.enums.Roles;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Login e cadastro de usuários")
@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Realiza login e retorna token JWT")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "400", description = "Formato inválido")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthenticationDto data){
        var emailPassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(emailPassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }



    // -- Método utilizado apenas para incluir um usuário ADMIN no banco de dados
    @PostMapping("/register/admin")
    public ResponseEntity<UsuarioResponseDto> registerAdmin(@RequestBody @Valid RegisterDto data) {
        if(this.repository.findByEmail(data.email()) != null)
            throw new BusinessRuleException("EMAIL_JA_CADASTRADO", "Este e-mail já está em uso.");

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        Usuario novoAdmin = new Usuario(data.nome(), data.email(), encryptedPassword, Roles.ROLE_ADMIN);
        this.repository.save(novoAdmin);

        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDto(novoAdmin));
    }


    @Operation(summary = "Cadastra um novo cliente")
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado")
    @ApiResponse(responseCode = "400", description = "E-mail já cadastrado")
    @PostMapping("/register/cliente")
    public ResponseEntity<UsuarioResponseDto> register(@RequestBody @Valid RegisterDto data){
        if(this.repository.findByEmail(data.email()) != null)
            throw new BusinessRuleException("EMAIL_JA_CADASTRADO", "Este e-mail já está em uso.");

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        Usuario novoUsuario = new Usuario(data.nome(), data.email(), encryptedPassword, Roles.ROLE_CLIENTE);

        this.repository.save(novoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDto(novoUsuario));
    }

    @Operation(summary = "Cadastra um funcionário - somente ADMIN")
    @ApiResponse(responseCode = "201", description = "Funcionário cadastrado")
    @ApiResponse(responseCode = "403", description = "Sem permissão")
    @PostMapping("/register/funcionario")
    @PreAuthorize("hasRole('ADMIN')") // APENAS usuários com token de ADMIN podem acessar
    public ResponseEntity<UsuarioResponseDto> registerEmployee(@RequestBody @Valid RegisterDto data) {

        // Regra de segurança: mesmo o Admin não pode criar outro Admin por aqui, só Funcionários (opcional)
        if (data.role() == Roles.ROLE_ADMIN) {
            throw new BusinessRuleException("OPERACAO_NAO_AUTORIZADA", "Operação não autorizada");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        Usuario novoUsuario = new Usuario(data.nome(), data.email(), encryptedPassword, Roles.ROLE_FUNCIONARIO);

        this.repository.save(novoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDto(novoUsuario));
    }
}

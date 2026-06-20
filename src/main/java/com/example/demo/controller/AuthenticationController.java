package com.example.demo.controller;

import com.example.demo.config.security.TokenService;
import com.example.demo.dtos.AuthenticationDto;
import com.example.demo.dtos.LoginResponseDto;
import com.example.demo.dtos.RegisterDto;
import com.example.demo.enums.Roles;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
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

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity email(@RequestBody @Valid AuthenticationDto data){
        var emailPassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(emailPassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register/cliente")
    public ResponseEntity register(@RequestBody @Valid RegisterDto data){
        if(this.repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel newUserModel = new UserModel(data.username(), data.email(), encryptedPassword, Roles.ROLE_CLIENTE);

        this.repository.save(newUserModel);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/employee")
    @PreAuthorize("hasRole('ADMIN')") // APENAS usuários com token de ADMIN podem acessar
    public ResponseEntity<Void> registerEmployee(@RequestBody @Valid RegisterDto data) {

        // Regra de segurança: mesmo o Admin não pode criar outro Admin por aqui, só Funcionários (opcional)
        if (data.role() == Roles.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel newUserModel = new UserModel(data.username(), data.email(), encryptedPassword, Roles.ROLE_FUNCIONARIO);

        this.repository.save(newUserModel);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}

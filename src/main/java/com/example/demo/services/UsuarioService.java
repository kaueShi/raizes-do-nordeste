package com.example.demo.services;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll(); // Inherited method
    }

    public Usuario saveUser(Usuario user) {
        if (usuarioRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already taken");
        }
        return usuarioRepository.save(user); // Inherited method
    }

    public Usuario getUserByUsername(String username) {
        return usuarioRepository.findByNome(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

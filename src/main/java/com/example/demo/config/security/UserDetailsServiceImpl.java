package com.example.demo.config.security;

import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Altere aqui para buscar pelo email, já que o Spring passa o identificador (email) como username
        UserDetails user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with email: " + username);
        }

        return user;
    }
}



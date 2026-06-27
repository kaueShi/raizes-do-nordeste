package com.example.demo.config.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, SecurityFilter securityFilter) throws Exception{
        return httpSecurity
                .csrf(crsf -> crsf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                            String jsonPreenchido = String.format(
                                    "{\"error\":\"TOKEN_AUSENTE_OU_INVALIDO\"," +
                                            "\"message\":\"Acesso não autorizado - Token ausente ou inválido.\"," +
                                            "\"details\":[]," +
                                            "\"timestamp\":\"%s\"," +
                                            "\"path\":\"%s\"," +
                                            "\"traceId\":\"%s\"}",
                                    java.time.Instant.now().toString(),
                                    request.getRequestURI(),
                                    java.util.UUID.randomUUID().toString()
                            );

                            response.getWriter().write(jsonPreenchido);
                        })
                )

                .authorizeHttpRequests(authorize -> authorize

                        //Auth - público
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register/cliente").permitAll()

                        //-> Método apenas para inserir admin no BD
                        // Endpoint de setup inicial — necessário para criar o primeiro Admin
                        // Deve ser desativado em ambiente de produção
                        .requestMatchers(HttpMethod.POST, "/auth/register/admin").permitAll()

                        //Swagger - público
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        //Cardápio público (cliente vê antes de fazer login)
                        .requestMatchers(HttpMethod.GET, "unidades/*/produtos").permitAll()

                        //Consultar unidades público
                        .requestMatchers(HttpMethod.GET, "/unidades").permitAll()

                        //Tudo mais exige autenticação (roles específicas via @PreAuthorize)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

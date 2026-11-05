package com.example.gerenciadorfisio.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private JwtFilter jwtFilter;

    // Impede que o JwtFilter rode duas vezes (Servlet e Spring Security).
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter filter) {
        FilterRegistrationBean<JwtFilter> registro = new FilterRegistrationBean<>(filter);
        registro.setEnabled(false);
        return registro;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Primeiro administrador criado pelo desktop: validado no UsuarioService
                        .requestMatchers(HttpMethod.POST, "/usuarios/admin").permitAll()

                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
                                "/swagger-resources/**", "/webjars/**",
                                "/auth/login", "/auth/login/esqueci-senha",
                                "/auth/login/recuperar-senha").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Administração de usuários
                        .requestMatchers("/usuarios/buscarusuariologado", "/auth/logout").authenticated()
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")

                        // Pacientes: recepção pode consultar/cadastrar
                        .requestMatchers(HttpMethod.GET, "/pacientes/**")
                        .hasAnyRole("ADMIN", "FISIOTERAPEUTA", "RECEPCAO")
                        .requestMatchers(HttpMethod.POST, "/pacientes/**")
                        .hasAnyRole("ADMIN", "FISIOTERAPEUTA", "RECEPCAO")
                        .requestMatchers("/pacientes/**")
                        .hasAnyRole("ADMIN", "FISIOTERAPEUTA")

                        // Tratamentos e sessões: equipe clínica
                        .requestMatchers("/tratamentos/**", "/sessoes/**")
                        .hasAnyRole("ADMIN", "FISIOTERAPEUTA")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

package com.example.gerenciadorfisio.configuration;

import com.example.gerenciadorfisio.application.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())
                || uri.startsWith("/swagger-ui")
                || uri.startsWith("/v2/api-docs")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-resources")
                || uri.startsWith("/webjars")
                || uri.equals("/auth/login")
                || uri.equals("/auth/login/esqueci-senha")
                || uri.equals("/auth/login/recuperar-senha")
                || (uri.equals("/usuarios/admin") && "POST".equalsIgnoreCase(request.getMethod()))) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Token não informado");
            return;
        }

        try {
            var usuario = tokenService.verificarToken(header.substring(7));
            var autenticacao = new UsernamePasswordAuthenticationToken(
                    usuario, null, usuario.authorities()
            );
            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Token inválido ou expirado");
            return;
        }

        // Não tratar como erro de token uma exceção de negócio dos Controllers.
        filterChain.doFilter(request, response);
    }
}

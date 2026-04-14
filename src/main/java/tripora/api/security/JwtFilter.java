//
//package tripora.api.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import tripora.api.config.JwtService;
//import tripora.api.domain.User;
//import tripora.api.repository.UserRepository;
//
//import java.io.IOException;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//    private final UserRepository userRepository;
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String path = request.getRequestURI();
//        log.info("=== JwtFilter hit: {}", path);
//
//        String authHeader = request.getHeader("Authorization");
//        log.info("=== Auth header: {}", authHeader);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            log.warn("=== No Bearer token, skipping auth");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//        log.info("=== Token extracted: {}", token);
//
//        String email;
//        try {
//            email = jwtService.extractEmail(token);
//            log.info("=== Email from token: {}", email);
//        } catch (Exception e) {
//            log.error("=== JWT parse failed: {}", e.getMessage());
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            log.info("=== Looking up user: {}", email);
//            User user = userRepository.findUserByEmail(email).orElseThrow();
//            log.info("=== User found: {}, roles: {}", user.getEmail(), user.getRoles());
//
//            var authorities = user.getRoles()
//                    .stream()
//                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
//                    .collect(Collectors.toList());
//
//            log.info("=== Authorities: {}", authorities);
//
//            UsernamePasswordAuthenticationToken authToken =
//                    new UsernamePasswordAuthenticationToken(email, null, authorities);
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//            log.info("=== Authentication set successfully");
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//}




package tripora.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tripora.api.config.JwtService;
import tripora.api.domain.User;
import tripora.api.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email;

        try {
            email = jwtService.extractEmail(token);
        } catch (Exception e) {
            log.warn("JWT parse failed: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtService.isTokenValid(token)) {
            log.warn("JWT token is expired or invalid for path: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        if (email == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user == null) {
            log.warn("No user found for email: {}", email);
            filterChain.doFilter(request, response);
            return;
        }

        List<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
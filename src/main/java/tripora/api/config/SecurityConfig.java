//
//
//package tripora.api.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import tripora.api.security.JwtFilter;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(auth -> auth
//
//                        // ================= PUBLIC =================
//                        .requestMatchers(HttpMethod.POST,
//                                "/v1/api/auth/register",
//                                "/v1/api/auth/login"
//                        ).permitAll()
//
//                        .requestMatchers(HttpMethod.GET,
//                                "/v1/api/tours",
//                                "/v1/api/tours/{id}",
//                                "/v1/api/tours/{id}/transport-options",
//                                "/v1/api/tours/{id}/reviews",
//                                "/v1/api/categories",
//                                "/v1/api/categories/{id}",
//                                "/v1/api/enums/payment-methods"
//                        ).permitAll()
//
//                        .requestMatchers(
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**"
//                        ).permitAll()
//
//                        .requestMatchers(HttpMethod.POST,
//                                "/v1/api/files/upload"
//                        ).permitAll()
//                        .requestMatchers(HttpMethod.GET,
//                                "/v1/api/guides")
//                        .permitAll()
//
//                        // ================= ADMIN ONLY =================
//                        .requestMatchers(HttpMethod.PATCH,
//                                "/v1/api/admin/users/{email}/role"
//                        ).hasRole("ADMIN")
//
//                        // Tours admin
//                        .requestMatchers(HttpMethod.POST,
//                                "/v1/api/tours",
//                                "/v1/api/tours/*/images",
//                                "/v1/api/tours/*/images/url",
//                                "/v1/api/tours/*/itineraries",
//                                "/v1/api/tours/*/transport-options"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.PUT,
//                                "/v1/api/tours/*",
//                                "/v1/api/tours/*/itineraries/*",
//                                "/v1/api/tours/*/transport-options/*"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.DELETE,
//                                "/v1/api/tours/*",
//                                "/v1/api/tours/*/images/*",
//                                "/v1/api/tours/*/itineraries/*",
//                                "/v1/api/tours/*/transport-options/*"
//                        ).hasRole("ADMIN")
//
//                        // Categories admin
//                        .requestMatchers(HttpMethod.POST, "/v1/api/categories").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/v1/api/categories/*").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/api/categories/*").hasRole("ADMIN")
//
//
//                        // GUIDES
//
//                        .requestMatchers(HttpMethod.POST,
//                                "/v1/api/guides"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.PUT,
//                                "/v1/api/guides/*"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.DELETE,
//                                "/v1/api/guides/*"
//                        ).hasRole("ADMIN")
//
//                        // Bookings admin
//                        .requestMatchers(HttpMethod.GET,
//                                "/v1/api/bookings/admin",
//                                "/v1/api/bookings/admin/*",
//                                "/v1/api/bookings/admin/by-date"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.PATCH,
//                                "/v1/api/bookings/admin/*/approve",
//                                "/v1/api/bookings/admin/*/cancel"
//                        ).hasRole("ADMIN")
//
//                        // ================= INVOICES =================
//                        .requestMatchers(HttpMethod.POST,
//                                "/v1/api/invoices/admin/*"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.GET,
//                                "/v1/api/invoices/admin",
//                                "/v1/api/invoices/admin/*"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.GET,
//                                "/v1/api/invoices/mine",
//                                "/v1/api/invoices/mine/*"
//                        ).hasRole("USER")
//
//                        // ================= PAYMENTS =================
//
//                        // USER pays invoice
//                        .requestMatchers(HttpMethod.POST,
//                                "/v1/api/payments/user/invoices/*/pay"
//                        ).hasRole("USER")
//
//                        // USER + ADMIN view payments
//                        .requestMatchers(HttpMethod.GET,
//                                "/v1/api/payments/user/invoices/*",
//                                "/v1/api/payments/admin/invoices/*"
//                        ).hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET,
//                                "/v1/api/payments/admin"
//                        ).hasAnyRole("ADMIN")
//
//                        // ================= USER FEATURES =================
//                        .requestMatchers("/v1/api/auth/me").hasRole("USER")
//
//                        .requestMatchers("/v1/api/bookings/mine/**").hasRole("USER")
//                        .requestMatchers(HttpMethod.POST, "/v1/api/bookings").hasRole("USER")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/api/bookings/mine/*").hasRole("USER")
//
//                        .requestMatchers(HttpMethod.POST, "/v1/api/reviews").hasRole("USER")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/api/reviews/*").hasRole("USER")
//
//                        .requestMatchers("/v1/api/wishlist/**").hasRole("USER")
//
//                        // ================= DEFAULT =================
//                        .anyRequest().denyAll()
//                )
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//}














package tripora.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tripora.api.security.JwtFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 1. ENABLE CORS FOR ALL CONTROLLERS
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // ================= PUBLIC =================
                        .requestMatchers(HttpMethod.POST,
                                "/v1/api/auth/register",
                                "/v1/api/auth/login"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/v1/api/tours",
                                "/v1/api/tours/{id}",
                                "/v1/api/tours/{id}/transport-options",
                                "/v1/api/tours/{id}/reviews",
                                "/v1/api/categories",
                                "/v1/api/categories/{id}",
                                "/v1/api/enums/payment-methods"
                        ).permitAll()

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST,
                                "/v1/api/files/upload"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/v1/api/guides")
                        .permitAll()

                        // ================= ADMIN ONLY =================
                        .requestMatchers(HttpMethod.PATCH,
                                "/v1/api/admin/users/{email}/role"
                        ).hasRole("ADMIN")

                        // Tours admin
                        .requestMatchers(HttpMethod.POST,
                                "/v1/api/tours",
                                "/v1/api/tours/*/images",
                                "/v1/api/tours/*/images/url",
                                "/v1/api/tours/*/itineraries",
                                "/v1/api/tours/*/transport-options"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/v1/api/tours/*",
                                "/v1/api/tours/*/itineraries/*",
                                "/v1/api/tours/*/transport-options/*"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/v1/api/tours/*",
                                "/v1/api/tours/*/images/*",
                                "/v1/api/tours/*/itineraries/*",
                                "/v1/api/tours/*/transport-options/*"
                        ).hasRole("ADMIN")

                        // Categories admin
                        .requestMatchers(HttpMethod.POST, "/v1/api/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/v1/api/categories/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/v1/api/categories/*").hasRole("ADMIN")


                        // GUIDES
                        .requestMatchers(HttpMethod.POST, "/v1/api/guides").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/v1/api/guides/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/v1/api/guides/*").hasRole("ADMIN")

                        // Bookings admin
                        .requestMatchers(HttpMethod.GET,
                                "/v1/api/bookings/admin",
                                "/v1/api/bookings/admin/*",
                                "/v1/api/bookings/admin/by-date"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH,
                                "/v1/api/bookings/admin/*/approve",
                                "/v1/api/bookings/admin/*/cancel"
                        ).hasRole("ADMIN")

                        // ================= INVOICES =================
                        .requestMatchers(HttpMethod.POST, "/v1/api/invoices/admin/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,
                                "/v1/api/invoices/admin",
                                "/v1/api/invoices/admin/*"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/v1/api/invoices/mine",
                                "/v1/api/invoices/mine/*"
                        ).hasRole("USER")

                        // ================= PAYMENTS =================
                        .requestMatchers(HttpMethod.POST, "/v1/api/payments/user/invoices/*/pay").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,
                                "/v1/api/payments/user/invoices/*",
                                "/v1/api/payments/admin/invoices/*"
                        ).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/api/payments/admin").hasAnyRole("ADMIN")

                        // ================= USER FEATURES =================
                        .requestMatchers("/v1/api/auth/me").hasRole("USER")
                        .requestMatchers("/v1/api/bookings/mine/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/v1/api/bookings").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/api/bookings/mine/*").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/v1/api/reviews").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/api/reviews/*").hasRole("USER")
                        .requestMatchers("/v1/api/wishlist/**").hasRole("USER")

                        // ================= DEFAULT =================
                        .anyRequest().denyAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 2. GLOBAL CORS POLICY - ALLOWS ALL CONTROLLERS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed Origin: Change this to "*" if you want to allow everyone,
        // but localhost:3000 is safer for your development.
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        // Allowed Methods: Covers all controller actions
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Allowed Headers: Critical for your JwtFilter to see the 'Authorization' header
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));

        // Credentials: Set to true to allow passing Bearer tokens/cookies
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Applying this configuration to ALL paths (/**)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
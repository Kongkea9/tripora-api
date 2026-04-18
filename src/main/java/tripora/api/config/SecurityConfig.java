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
//        http
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//
//                        // PUBLIC: Auth
//                        .requestMatchers(HttpMethod.POST,
//                                "/v1/api/auth/register",
//                                "/v1/api/auth/login"
//                        ).permitAll()
//
//                        // PUBLIC: Tours (read-only)
//                        .requestMatchers(HttpMethod.GET,
//                                "/v1/api/tours",
//                                "/v1/api/tours/{id}",
//                                "/v1/api/tours/{id}/transport-options",
//                                "/v1/api/tours/{id}/reviews"
//                        ).permitAll()
//
//                        // PUBLIC: Categories (read-only)
//                        .requestMatchers(HttpMethod.GET,
//                                "/v1/api/categories",
//                                "/v1/api/categories/{id}"
//
//                        ).permitAll()
//
//                        // Swagger
//                        .requestMatchers(
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**"
//                        ).permitAll()
//
//                        .requestMatchers(HttpMethod.POST,
//                                "/v1/api/files/upload"
//                        ).permitAll()
//
//                        .requestMatchers(HttpMethod.PATCH,
//                                "/v1/api/admin/users/{email}/role"
//                        ).hasRole("ADMIN")
//
//                        // ADMIN: Sales Dashboard
//                        .requestMatchers(HttpMethod.GET,
//                                "/v1/api/admin/sales/summary",
//                                "/v1/api/admin/sales/revenue-by-month",
//                                "/v1/api/admin/sales/top-tours",
//                                "/v1/api/admin/sales/transport-breakdown",
//                                "/v1/api/admin/sales/pending-actions"
//                        ).hasRole("ADMIN")
//
//                        // ADMIN: Tours (write)
//                        .requestMatchers(HttpMethod.POST,
//                                "/v1/api/tours",
//                                "/v1/api/tours/{id}/images",
//                                "/v1/api/tours/{id}/images/url",
//
//                                "/v1/api/tours/{id}/itineraries",
//                                "/v1/api/tours/{id}/transport-options"
//                        ).hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT,
//                                "/v1/api/tours/{id}",
//                                "/v1/api/tours/{id}/itineraries/{dayId}",
//                                "/v1/api/tours/{id}/transport-options/{optId}"
//                        ).hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE,
//                                "/v1/api/tours/{id}",
//                                "/v1/api/tours/{id}/images/{imgId}",
//                                "/v1/api/tours/{id}/itineraries/{dayId}",
//                                "/v1/api/tours/{id}/transport-options/{optId}"
//                        ).hasRole("ADMIN")
//
//                        // ADMIN: Categories (write)
//                        .requestMatchers(HttpMethod.POST,  "/v1/api/categories").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT,   "/v1/api/categories/{id}").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE,"/v1/api/categories/{id}").hasRole("ADMIN")
//
//                        // ADMIN: Guides
//                        .requestMatchers(
//                                "/v1/api/guides",
//                                "/v1/api/guides/{id}"
//                        ).hasRole("ADMIN")
//
//                        // ADMIN: Bookings
//                        .requestMatchers(HttpMethod.GET,
//                                "/v1/api/bookings/admin",
//                                "/v1/api/bookings/admin/{id}",
//                                "/v1/api/bookings/admin/by-date"
//                        ).hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PATCH,
//                                "/v1/api/bookings/admin/{id}/approve",
//                                "/v1/api/bookings/admin/{id}/cancel"
//                        ).hasRole("ADMIN")
//
////                        // ADMIN: Invoices
////                        .requestMatchers(HttpMethod.GET,
////                                "/v1/api/invoices/admin",
////                                "/v1/api/invoices/admin/{id}",
//////                                "/v1/api/invoices/admin/{id}/payments"
////                        ).hasRole("ADMIN")
////                        .requestMatchers(HttpMethod.PATCH,
////                                "/v1/api/admin/invoices/{id}/status"
////                        ).hasRole("ADMIN")
//
//
//                                // ================= INVOICES =================
//
//                                .requestMatchers(HttpMethod.GET,
//                                        "/v1/api/invoices/mine",
//                                        "/v1/api/invoices/mine/**"
//                                ).hasRole("USER")
//
//                                .requestMatchers(HttpMethod.GET,
//                                        "/v1/api/invoices/admin",
//                                        "/v1/api/invoices/admin/**"
//                                ).hasRole("ADMIN")
//
//                                .requestMatchers(HttpMethod.POST,
//                                        "/v1/api/invoices/admin/**"
//                                ).hasRole("ADMIN")
//
////                        .requestMatchers(HttpMethod.POST,
////                                "/v1/api/admin/invoices/{id}/payments"
////                        ).hasRole("ADMIN")
//
//                        // ================= PAYMENTS =================
//
//                        .requestMatchers(HttpMethod.POST,
//                                "/v1/api/payments/user/invoices/{invoiceId}/pay"
//                        ).hasRole("USER")
//
//                        .requestMatchers(HttpMethod.GET,
//                                "/v1/api/payments/user/invoices/{invoiceId}",
//                                "/v1/api/payments/admin/invoices/{invoiceId}"
//                        ).hasAnyRole("USER", "ADMIN")
//
//                        // ADMIN: Reviews
//                        .requestMatchers(HttpMethod.DELETE,
//                                "/v1/api/admin/reviews/{id}"
//                        ).hasRole("ADMIN")
//
//                        // USER: Auth profile
//                        .requestMatchers(
//                                "/v1/api/auth/me"
//                        ).hasRole("USER")
//
//                        // USER: Bookings
//                        .requestMatchers(
//                                "/v1/api/bookings/mine",
//                                "/v1/api/bookings/mine/{id}"
//                        ).hasRole("USER")
//                        .requestMatchers(HttpMethod.POST,  "/v1/api/bookings").hasRole("USER")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/api/bookings/mine/{id}").hasRole("USER")
//
//                        // USER: Invoices
//                        .requestMatchers(
//                                "/v1/api/invoices/mine",
//                                "/v1/api/invoices/mine/{id}"
//                        ).hasRole("USER")
//
//                        // USER: Reviews
//                        .requestMatchers(HttpMethod.POST,   "/v1/api/reviews").hasRole("USER")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/api/reviews/{id}").hasRole("USER")
//
//                        // USER: Wishlist
//                        .requestMatchers(
//                                "/v1/api/wishlist",
//                                "/v1/api/wishlist/{tourId}"
//                        ).hasRole("USER")
//
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
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tripora.api.security.JwtFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
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

                        .requestMatchers(HttpMethod.POST,
                                "/v1/api/guides"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/v1/api/guides/*"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/v1/api/guides/*"
                        ).hasRole("ADMIN")

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
                        .requestMatchers(HttpMethod.POST,
                                "/v1/api/invoices/admin/*"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/v1/api/invoices/admin",
                                "/v1/api/invoices/admin/*"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/v1/api/invoices/mine",
                                "/v1/api/invoices/mine/*"
                        ).hasRole("USER")

                        // ================= PAYMENTS =================

                        // USER pays invoice
                        .requestMatchers(HttpMethod.POST,
                                "/v1/api/payments/user/invoices/*/pay"
                        ).hasRole("USER")

                        // USER + ADMIN view payments
                        .requestMatchers(HttpMethod.GET,
                                "/v1/api/payments/user/invoices/*",
                                "/v1/api/payments/admin/invoices/*"
                        ).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET,
                                "/v1/api/payments/admin"
                        ).hasAnyRole("ADMIN")

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
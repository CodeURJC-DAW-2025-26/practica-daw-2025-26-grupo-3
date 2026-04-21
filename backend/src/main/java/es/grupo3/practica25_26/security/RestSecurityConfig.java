package es.grupo3.practica25_26.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.grupo3.practica25_26.security.jwt.JwtRequestFilter;
import es.grupo3.practica25_26.security.jwt.JwtTokenProvider;
import es.grupo3.practica25_26.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@Order(1) // This security first
public class RestSecurityConfig {

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        // The method configuration only applies to URLs that begin with /api
        http.securityMatcher("/api/**");

        http.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

        http.authenticationProvider(restAuthenticationProvider());

        http.authorizeHttpRequests(authorize -> authorize
                // ADMIN ONLY API REST URLS
                .requestMatchers(HttpMethod.GET, "/api/v1/admin/**").hasRole("ADMIN")
                // 📖 PERMITIR OPENAPI / SWAGGER UI (Añade esta línea)
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // PRIVATE API REST URLS
                .requestMatchers(HttpMethod.GET, "/api/v1/products/*/reviews").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/users/logged").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasAnyRole("USER", "ADMIN")
                // PUBLIC API REST URLS
                .anyRequest().permitAll()

        );

        // Disable form login from API REST
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection for API REST
        http.csrf(csrf -> csrf.disable());

        // Enable basic authentication
        http.httpBasic(basic -> basic.disable());

        // Stateless Session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT Token filter (intercepts the token on each request)
        http.addFilterBefore(new JwtRequestFilter(userDetailsService, jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider restAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
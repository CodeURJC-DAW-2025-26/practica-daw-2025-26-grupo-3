package es.grupo3.practica25_26.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import es.grupo3.practica25_26.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PUBLIC PAGES
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/user_register").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/loginerror").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/product_search/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/user-images/**").permitAll()
                        .requestMatchers("/product-images/**").permitAll()
                        // PRIVATE PAGES
                        .requestMatchers("/product_detail/*").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/product-publish/*").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/profile/*").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shopping-cart").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin_panel/").hasAnyRole("ADMIN"))
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login/getUser")
                        .usernameParameter("email")
                        .failureUrl("/loginerror")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());

        return http.build();
    }
}

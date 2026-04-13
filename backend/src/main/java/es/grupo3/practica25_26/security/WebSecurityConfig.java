package es.grupo3.practica25_26.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/images/**"))
                .authorizeHttpRequests(authorize -> authorize
                        // PUBLIC PAGES
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/orders").permitAll()
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/user_register").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/loginerror").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/product_search/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/user-images/**").permitAll()
                        .requestMatchers("/product-images/**").permitAll()
                        .requestMatchers("/products/more/**").permitAll()
                        .requestMatchers("/v3/api-docs.yml").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/new/**").permitAll()
                        // we allow everyone to access the blocked user error page, so that if a user
                        // tries to log in with a blocked account, they will be redirected to this page
                        // and informed about their account status.
                        .requestMatchers("/blocked_user_error").permitAll()
                        .requestMatchers("/access_denied").permitAll()
                        // PRIVATE PAGES
                        .requestMatchers("/product_detail/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/product-publish/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/edit_review/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/delete_review/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/profile/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shopping-cart").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/cart/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin_panel/**").hasAnyRole("ADMIN")
                        .requestMatchers("/admin_profile/**").hasAnyRole("ADMIN")
                        .requestMatchers("/edit_product_form_admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/orders_list/**").hasAnyRole("ADMIN")
                        .requestMatchers("/orders/*/accept").hasAnyRole("ADMIN")
                        .requestMatchers("/products_published/**").hasAnyRole("ADMIN")
                        .requestMatchers("/user_registered_list/**").hasAnyRole("ADMIN")
                        .requestMatchers("/my_products/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/publish_new_product/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/bill/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/delete_product/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/add_review/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/edit_product/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("ADMIN"))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access_denied"))
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login/getUser")
                        .successHandler(loginSuccessHandler)
                        .usernameParameter("email")

                        // ---> NEW: Replace .failureUrl() with this:
                        .failureHandler((request, response, exception) -> {
                            if (exception instanceof org.springframework.security.authentication.DisabledException) {
                                // If disabled, redirect to the blocked user page
                                response.sendRedirect("/blocked_user_error");
                            } else {
                                // If it failed for another reason (bad password), go to the normal error
                                response.sendRedirect("/loginerror");
                            }
                        }))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .permitAll());

        return http.build();
    }
}
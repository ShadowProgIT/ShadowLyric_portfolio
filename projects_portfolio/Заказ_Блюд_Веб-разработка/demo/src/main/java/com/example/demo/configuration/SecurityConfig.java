package com.example.demo.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/users/login", "/users/register").permitAll()
                        .requestMatchers("/users/profile/**", "/dishes/", "/dishes/top").authenticated()
                        .requestMatchers("/users", "/users/**").hasRole("ADMIN")
                        .requestMatchers("/dishes/add", "/dishes/delete").hasAnyRole("MODERATOR", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/users/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/errors/403")
                );
        return http.build();
    }
}
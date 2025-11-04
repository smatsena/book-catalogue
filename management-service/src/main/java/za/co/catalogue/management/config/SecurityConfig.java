package za.co.catalogue.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable for REST API; enable later for forms
                .authorizeHttpRequests(auth -> auth
                        // public endpoints
                        .requestMatchers("/h2-console/**").permitAll()

                        // Book APIs — role-based control
                        .requestMatchers(HttpMethod.GET, "/api/books/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/books/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")

                        // everything else requires authentication
                        .anyRequest().authenticated()
                )
                // allow H2 console in browser frames
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                // use simple Basic Auth for now
                .httpBasic();

        return http.build();
    }

    @Bean
    UserDetailsService users() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN") // full privileges
                .build();

        UserDetails librarian = User.withUsername("librarian")
                .password(passwordEncoder().encode("books123"))
                .roles("USER") // limited privileges
                .build();

        return new InMemoryUserDetailsManager(admin, librarian);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

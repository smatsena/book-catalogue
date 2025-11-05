package za.co.catalogue.management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.security.users[0].username}")
    private String username_admin;
    @Value("${app.security.users[0].password}")
    private String password_admin;

    @Value("${app.security.users[1].username}")
    private String username_worker;
    @Value("${app.security.users[1].password}")
    private String password_worker;

    @Value("${app.security.users[0].roles}")
    private String admin_role;
    @Value("${app.security.users[1].roles}")
    private String worker_role;

    /**
     * Configures the security filter chain for HTTP requests.
     * 
     * <p>Defines the following access rules:
     * <ul>
     *   <li>H2 console: Public access (development only)</li>
     *   <li>GET /api/books: Admin and Worker roles</li>
     *   <li>POST /api/books: Admin and Worker roles</li>
     *   <li>PATCH /api/books: Admin role only</li>
     *   <li>DELETE /api/books: Admin role only</li>
     *   <li>All other requests: Requires authentication</li>
     * </ul>
     * 
     * @param http HttpSecurity instance to configure
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // REST-friendly defaults
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .headers().frameOptions().sameOrigin().and() // allow H2 console

                .authorizeRequests()
                // ---- public endpoints ----
                .antMatchers("/h2-console/**").permitAll()
                //.antMatchers("/actuator/health", "/actuator/health/**").permitAll()
                //.antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // ---- books API (role-based) ----
                .antMatchers(HttpMethod.GET, "/api/books/**").hasAnyRole(worker_role, admin_role)
                .antMatchers(HttpMethod.POST, "/api/books/**").hasAnyRole(worker_role, admin_role)
                .antMatchers(HttpMethod.PATCH, "/api/books/**").hasRole(admin_role)
                .antMatchers(HttpMethod.DELETE, "/api/books/**").hasRole(admin_role)

                // everything else requires auth
                .anyRequest().authenticated()
                .and()
                // Basic auth for now (easy with Postman)
                .httpBasic();

        return http.build();
    }

    /**
     * Creates an in-memory user details service with admin and worker users.
     * 
     * <p>The users are created from configuration values and passwords are
     * encoded using the provided password encoder.
     * 
     * @param encoder Password encoder for hashing passwords
     * @return UserDetailsService with configured users
     */
    @Bean
    UserDetailsService users(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername(this.username_admin).password(encoder.encode(this.password_admin)).roles(this.admin_role).build(),
                User.withUsername(this.username_worker).password(encoder.encode(this.password_worker)).roles(this.worker_role).build()
        );
    }

    /**
     * Provides a BCrypt password encoder for hashing user passwords.
     * 
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

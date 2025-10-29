package learn.studentmanagment.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (not needed for stateless REST APIs)
                .csrf(csrf -> csrf.disable())

                // 2. Set Session Management to STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Define URL-based authorization rules
                .authorizeHttpRequests((authorize) ->
                        authorize
                                // Allow public access to register and docs
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html"
                                ).permitAll()
                                .requestMatchers("/api/register/**").permitAll()
                                // STUDENT permissions
                                .requestMatchers(HttpMethod.PUT, "/api/profile/edit").hasRole("STUDENT")
                                .requestMatchers(HttpMethod.GET, "/api/sources/download/**").hasRole("STUDENT")

                                // TEACHER permissions
                                .requestMatchers("/api/attendance/**").hasRole("TEACHER")
                                .requestMatchers("/api/sources/upload").hasRole("TEACHER")

                                // GENERAL STUDENT/TEACHER permissions
                                .requestMatchers(HttpMethod.GET, "/api/students").hasAnyRole("STUDENT", "TEACHER", "ADMIN")

                                // ADMIN permissions (CUD on students)
                                .requestMatchers(HttpMethod.POST, "/api/students").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/students/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/students/**").hasRole("ADMIN")

                                // All other requests must be authenticated
                                .anyRequest().authenticated()
                )

                // 4. Use HTTP Basic Authentication
                .httpBasic(Customizer.withDefaults()); // This prompts Postman for username/password

        return http.build();
    }
}
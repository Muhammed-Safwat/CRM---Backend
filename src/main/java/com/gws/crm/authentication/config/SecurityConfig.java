package com.gws.crm.authentication.config;

import com.gws.crm.authentication.filters.JwtTokenFilter;
import com.gws.crm.authentication.filters.TransitionBuilderFilter;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.authentication.utils.JwtTokenService;
import com.gws.crm.common.utils.TransitionUtilHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenService jwtTokenService;

    private final UserRepository userRepository;

    private final TransitionUtilHandler transitionUtilHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200",
                "http://localhost:8080", "https://www.estshir.com", "https://estshir.com", "https://www.rnbinvesment.com", "www.rnbinvesment.com", "https://rnbinvesment.com"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // cors configuration
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((requests) ->
                        requests.requestMatchers(
                                        "/auth/**",
                                        "/mail/**",
                                        "/oauth2/**",
                                        "/world/**",
                                        "/company-profile/company-is-mobile-exist",
                                        "/APIs/countryCodes",
                                        "/APIs/jobNames",
                                        "/v2/api-docs",
                                        "/v3/api-docs/**",
                                        "/configuration/ui",
                                        "/swagger-resources/**",
                                        "/configuration/security",
                                        "/swagger-ui.html",
                                        "/webjars/**",
                                        "/swagger-ui/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/lookups/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/employees/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/api/super-admin/admins/**").hasRole("SUPER_ADMIN")
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/employees/**").hasRole("ADMIN")
                                .requestMatchers("/api/lookups/**").hasRole("ADMIN")
                                .requestMatchers("/api/privileges/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(transitionBuilderFilter(), UsernamePasswordAuthenticationFilter.class);
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtTokenService, userRepository);
    }

    @Bean
    public TransitionBuilderFilter transitionBuilderFilter() {
        return new TransitionBuilderFilter(jwtTokenService, transitionUtilHandler);
    }


}

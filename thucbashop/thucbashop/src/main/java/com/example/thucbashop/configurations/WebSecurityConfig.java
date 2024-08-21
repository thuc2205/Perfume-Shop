package com.example.thucbashop.configurations;

import com.example.thucbashop.filters.JwtTokenFilter;
import com.example.thucbashop.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebMvc
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiFix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(
                                    String.format("%s/users/register", apiFix),
                                    String.format("%s/users/login", apiFix)
                            )
                            .permitAll()
                            .requestMatchers(GET,String.format("%s/roles**",apiFix)).permitAll()

                            .requestMatchers(GET,String.format("%s/categories**",apiFix)).permitAll()
                            .requestMatchers(POST,String.format("%s/categories/**",apiFix)).hasAnyRole(Role.ADMIN)
                            .requestMatchers(PUT,String.format("%s/categories/**",apiFix)).hasAnyRole(Role.ADMIN)
                            .requestMatchers(DELETE,String.format("%s/categories/**",apiFix)).hasAnyRole(Role.ADMIN)

                            .requestMatchers(GET,String.format("%s/products**",apiFix)).permitAll()
                            .requestMatchers(GET,String.format("%s/products/**",apiFix)).permitAll()
                            .requestMatchers(POST,String.format("%s/products/**",apiFix)).hasAnyRole(Role.ADMIN)
                            .requestMatchers(PUT,String.format("%s/products/**",apiFix)).hasAnyRole(Role.ADMIN)
                            .requestMatchers(DELETE,String.format("%s/products/**",apiFix)).hasAnyRole(Role.ADMIN)
                            .requestMatchers(GET,String.format("%s/products/images/*",apiFix)).permitAll()


                            .requestMatchers(PUT,String.format("%s/orders/**",apiFix)).hasRole(Role.ADMIN)
                            .requestMatchers(POST,String.format("%s/orders/**",apiFix)).permitAll()
                            .requestMatchers(DELETE,String.format("%s/orders/**",apiFix)).hasRole(Role.ADMIN)
                            .requestMatchers(GET,String.format("%s/orders/**",apiFix)).hasAnyRole(Role.USER,Role.ADMIN)

                            .requestMatchers(PUT,String.format("%s/order_details/**",apiFix)).hasRole(Role.ADMIN)
                            .requestMatchers(POST,String.format("%s/order_details/**",apiFix)).hasAnyRole(Role.USER)
                            .requestMatchers(DELETE,String.format("%s/order_details/**",apiFix)).hasRole(Role.ADMIN)
                            .requestMatchers(GET,String.format("%s/order_details/**",apiFix)).hasAnyRole(Role.USER,Role.ADMIN)
                            .anyRequest().authenticated();
                });

        http.cors(httpSecurityCorsConfigurer -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("*"));
            configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("authorization","content-type","x-auth-token"));
            configuration.setExposedHeaders(List.of("x-auth-token"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**",configuration);
            httpSecurityCorsConfigurer.configurationSource(source);
        });
        return http.build();

    }
}

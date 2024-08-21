package com.example.thucbashop.configurations;

import com.example.thucbashop.models.User;
import com.example.thucbashop.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//hỗ trợ tokken
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepo userRepo;

    //userdetail
    //khi bật app lên khởi tạo 1 đối tg userdetail chính là user của ta
    //user của ta đã tực thi interface UserDetail để thỏa mãn UserDetail
    @Bean
    public UserDetailsService userDetailsService() {
        return phoneNumber ->
                userRepo.findByPhoneNumber(phoneNumber).
                        orElseThrow(() -> new UsernameNotFoundException("Cannot find user with phone Number - " + phoneNumber));
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}

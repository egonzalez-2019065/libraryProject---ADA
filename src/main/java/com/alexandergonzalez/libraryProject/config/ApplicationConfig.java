package com.alexandergonzalez.libraryProject.config;

import com.alexandergonzalez.libraryProject.repositories.auth.AuthJPARepository;
import com.alexandergonzalez.libraryProject.repositories.auth.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Configuration
public class ApplicationConfig {

    @Value("${databaseType}")
    private String databaseType;
    private final AuthRepository authRepository;
    private final AuthJPARepository authJPARepository;

    @Autowired
    public ApplicationConfig(AuthRepository authRepository, AuthJPARepository authJPARepository) {
        this.authRepository = authRepository;
        this.authJPARepository = authJPARepository;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return  config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return  authenticationProvider;
    }

    @Bean public UserDetailsService userDetailsService() {
        if ("JPA".equalsIgnoreCase(databaseType)) {
            return username -> authJPARepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        } else if ("MONGO".equalsIgnoreCase(databaseType)) {
            return username -> authRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        }
        throw new IllegalArgumentException("Invalid database type: " + databaseType);
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

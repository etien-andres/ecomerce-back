package com.lolacosmetics.ecomerce_back.config;
import com.lolacosmetics.ecomerce_back.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
    @EnableWebSecurity
    public class SecurityConfig  {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Autowired
    AuthorizationFilter authorizationFilter;

    @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)  {
            http
                    .authorizeHttpRequests((authorize) -> authorize
                            .requestMatchers("/api/authenticate").permitAll()
                            .requestMatchers(HttpMethod.POST,"/api/usuarios/registro").permitAll()
                            .requestMatchers("/**").hasAuthority("USUARIO")
                    ).authenticationProvider(authenticationProvider()).addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)

                    .csrf(csrf -> csrf
                   .disable().authorizeHttpRequests((authorize) ->authorize.requestMatchers("/api/usuarios/registro").permitAll().
                            requestMatchers("/api/authenticate").permitAll()
                   )
            );


            return http.build();
        }



    }
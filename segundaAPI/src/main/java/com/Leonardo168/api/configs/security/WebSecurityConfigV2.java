package com.Leonardo168.api.configs.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfigV2 {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .httpBasic(withDefaults())
	        .authorizeHttpRequests(authorizeRequests ->
	            authorizeRequests
	                .requestMatchers(HttpMethod.GET, "/user").hasAuthority("ROLE_ADMIN")
	                .requestMatchers(HttpMethod.POST, "/user").permitAll()
	                .requestMatchers(HttpMethod.PUT, "/user").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
	                .requestMatchers(HttpMethod.PUT, "/user/admin/{id}").hasAuthority("ROLE_ADMIN")
	                .requestMatchers(HttpMethod.DELETE, "/user/self").hasAuthority("ROLE_USER")
	                .requestMatchers(HttpMethod.DELETE, "/user/{id}").hasAuthority("ROLE_ADMIN")
	                .requestMatchers(HttpMethod.DELETE, "/user/definitivo/{id}").hasAuthority("ROLE_ADMIN")
	                
	                .requestMatchers(HttpMethod.GET, "/product").permitAll()
	                .requestMatchers(HttpMethod.GET, "/product/title/{title}").permitAll()
	                .requestMatchers(HttpMethod.GET, "/product/isbn/{isbn}").permitAll()
	                .requestMatchers(HttpMethod.GET, "/product/author/{author}").permitAll()
	                .requestMatchers(HttpMethod.GET, "/product/category/{category}").permitAll()
	                .requestMatchers(HttpMethod.POST, "/product").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
	                .requestMatchers(HttpMethod.PUT, "/product/{productId}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
	                .requestMatchers(HttpMethod.DELETE, "/product/definitivo/{productId}").hasAuthority("ROLE_ADMIN")
	                
	                .requestMatchers(HttpMethod.GET, "/category").permitAll()
	                .requestMatchers(HttpMethod.POST, "/category").hasAuthority("ROLE_ADMIN")
	                .requestMatchers(HttpMethod.PUT, "/category/{categoryName}").hasAuthority("ROLE_ADMIN")
	                .requestMatchers(HttpMethod.DELETE, "/category/{categoryName}").hasAuthority("ROLE_ADMIN")
	                .requestMatchers(HttpMethod.DELETE, "/category/definitivo/{categoryName}").hasAuthority("ROLE_ADMIN")
	                
	                .requestMatchers(HttpMethod.POST, "/transaction/{productId}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
	                .requestMatchers(HttpMethod.GET, "/transaction/{buyerId}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
	                .requestMatchers(HttpMethod.DELETE, "/transaction/definitivo/{transactionId}").hasAuthority("ROLE_ADMIN")
	                
	                .anyRequest().authenticated()
	        )
	        .csrf(csrf -> csrf.disable());
	    return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}

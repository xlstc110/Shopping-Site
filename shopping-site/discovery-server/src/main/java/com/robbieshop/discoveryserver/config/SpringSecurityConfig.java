//package com.robbieshop.discoveryserver.config;
//
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity//from spring security project, not webflux project
//public class SpringSecurityConfig{
//    //For this class, need to study spring security 3.0
//    @Bean
//    public SecurityFilterChain filterChain(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.inMemoryAuthentication()
//                .withUser("Eureka").password("password")
//                .authorities("USER");
//        return http.build();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(spec -> spec.disable())
//                .authorizeHttpRequests(Customizer.withDefaults()).anyRequest()
//                .;
//        return http.build();
//    }
//}

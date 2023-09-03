//package config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//@Configuration
//@EnableWebFluxSecurity//enable web security for web-flux project, since the Spring Cloud Gateway project is based on Web Flux project, not Spring Web MVC
//public class SecurityConfig {
//
//    @Bean
//    //In Spring Security 6.1 both csrf() and jwt() are deprecated, so the code only works for version below 6.0
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity){
//
//        //first disable csrf() because we are only communicating the REST API through PostMan
//        //path matcher to the eureka uri so that when we are accessing the static resources, we do not need to send token with it.
//        //therefore, we need to exclude this call from our security configuration.
////        serverHttpSecurity.csrf()
////                .disable()
////                .authorizeExchange(exchange -> exchange
////                        .pathMatchers("/eureka/**")
////                        .permitAll()
////                        .anyExchange()
////                        .authenticated())
////                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
//        serverHttpSecurity.csrf(spec -> spec.disable());
//
//// For OAuth2 Resource Server, assuming jwt() is deprecated, find an alternative based on the latest library.
//        serverHttpSecurity.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
//
//// Authorize Exchange setup remains the same.
//        serverHttpSecurity.authorizeExchange(exchange -> exchange
//                .pathMatchers("/eureka/**")
//                .permitAll()
//                .anyExchange()
//                .authenticated());
//
//        //return the SecurityWebFilterChain object after the configuration
//        return serverHttpSecurity.build();
//    }
//}

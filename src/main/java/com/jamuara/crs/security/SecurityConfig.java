package com.jamuara.crs.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    @Value("${app.keycloak.admin.clientId}")
    private String keycloakClientId;

//    @Bean
//    CorsConfigurationSource configurationSource() {
////            CorsConfigurationSource source = request -> {
////                    ConfigurationSource source = request -> {
//                CorsConfiguration config = new CorsConfiguration();
//                config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
//                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//                config.setAllowedHeaders(List.of("*"));
//                config.setAllowCredentials(false);
////                return config;
////            };
////            c.configurationSource(source);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }

    @Bean
    SecurityFilterChain resourceSecurityFilterChain(
            HttpSecurity http,
            Converter<Jwt, AbstractAuthenticationToken> authenticationTokenConverter
    ) throws Exception {
        http.oauth2ResourceServer(resourceServer -> {
            resourceServer.jwt(jwtDecoder -> {
                jwtDecoder.jwtAuthenticationConverter(authenticationTokenConverter);
            });
        });

        http.sessionManagement(sessions -> {
                    sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).csrf(csrf -> csrf.disable())
//                .cors(Customizer.withDefaults());
                .cors(c -> {
                    CorsConfigurationSource source = request -> {
//                    ConfigurationSource source = request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173", "http://localhost:8081"));
                        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        config.setAllowedHeaders(List.of("*"));
                        config.setAllowCredentials(false);
                        return config;
                    };
                    c.configurationSource(source);
                });

        http.authorizeHttpRequests(requests -> {
            requests.requestMatchers("/secured").authenticated();
           // requests.requestMatchers("/api/users").authenticated();
            requests.requestMatchers("/user-profile").authenticated();
//            requests.requestMatchers("/flights/flight-order").authenticated();
            requests.anyRequest().permitAll();
        });

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter authenticationConverter(AuthoritiesConverter authoritiesConverter) {
        var authenticationConverter = new  JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            return authoritiesConverter.convert(jwt.getClaims());
        });
        authenticationConverter.setPrincipalClaimName("preferred_username");
        return authenticationConverter;
    }

    @Bean
    AuthoritiesConverter realmRolesConverter() {
        return (claims) -> {
//            var realmAccess = Optional.ofNullable(
//                    (Map<String, Object>) claims.get("realm_access"));
//            var resourceAccess = Optional.ofNullable(
//                    (Map<String, Object>) claims.get("resource_access"));
//
//            var roles = resourceAccess.flatMap(map -> Optional.ofNullable(
//                    (List<String>) map.get("roles")));
////            roles.map(List::stream)
////                    .orElse(Stream.empty())
//            return roles.stream().flatMap(Collection::stream)
//                    .map(SimpleGrantedAuthority::new)
//                    .map(GrantedAuthority.class::cast)
//                    .toList();

            List<String> roles = new ArrayList<>();

            Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                roles.addAll((Collection<String>) realmAccess.get("roles"));
            }

            Map<String, Object> resourceAccess = (Map<String, Object>) claims.get("resource_access");
            if (resourceAccess != null) {
                Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(keycloakClientId);
                if (clientAccess != null && clientAccess.containsKey("roles")) {
                    roles.addAll((Collection<String>) clientAccess.get("roles"));
                }
            }

            return roles.stream()
                    .map(role -> "ROLE_" + role.toUpperCase())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        };
    }
}

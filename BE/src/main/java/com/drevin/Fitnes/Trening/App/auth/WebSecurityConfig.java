package com.drevin.Fitnes.Trening.App.auth;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

/*@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)*/
@RestController
public class WebSecurityConfig  {

    /*
   private JwtEncoder jwtEncoder;

    public WebSecurityConfig(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/authenticate")
    public JwtResponse authentication (Authentication authentication){
        return new JwtResponse(createToken(authentication));
    }*/

    @GetMapping("/login")
    public String loginPage(){
        return "login Page";
    }

    @GetMapping("/api/v1/auth")
    public String auth(){
        return "success";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "reg Page";
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    // hasAuthority neocekava zadny prefix, zatim co hasRole ocekava prefix ROLE_
    public String test(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        boolean hasUserRole = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("SCOPE_ROLE_USER"));
        if (hasUserRole) {
            return "Hello user! you are logged" + authentication.getAuthorities().toString();
        } else {
            return "Hello admin!" + authentication.getAuthorities().toString();
        }
    }

/*
    private String createToken(Authentication authentication) {
        var claims =  JwtClaimsSet
                .builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60))
                .subject(authentication.getName())
                .claim("scope",createScope(authentication))
                .build();


        JwtEncoderParameters parameters = JwtEncoderParameters.from(claims) ;
        return jwtEncoder.encode(parameters).getTokenValue();
    }

    private String createScope(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map( a -> a.getAuthority().substring(6))
                .collect(Collectors.joining(" "));
    }
*/


}

/*
record JwtResponse (String token) {}*/

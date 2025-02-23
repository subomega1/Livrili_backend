package com.sfar.livrili.Service.Impl;

import com.sfar.livrili.Domains.Entities.User;
import com.sfar.livrili.Repositories.UserRepository;
import com.sfar.livrili.Service.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.secret}")
    private String secret;

    private final long  jwtExpirationTime = 86400000L;

    @Override
    public UserDetails authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return  userDetailsService.loadUserByUsername(email);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
       Map<String, Object> claims = new HashMap<>();
       claims.put("role", userDetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("Client"));
       return Jwts.builder()
               .setClaims(claims)
               .setSubject(userDetails.getUsername())
               .issuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
               .signWith(getSecretKey(),SignatureAlgorithm.HS256).compact();

    }

    @Override
    public UserDetails validateToken(String token) {
        String username = extractUsername(token);
        return userDetailsService.loadUserByUsername(username );
    }

   public String extractUsername(String token) {
       Claims claims = Jwts.parser()
               .setSigningKey(getSecretKey())
               .build()
               .parseClaimsJws(token)
               .getBody();
       return claims.getSubject();
    }
    public String extractRole(String token) {
        Claims claims = Jwts.parser().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
        return claims.get("role", String.class);

    }


    private Key getSecretKey() {
       byte[] secretKey = secret.getBytes();
       return Keys.hmacShaKeyFor(secretKey);
    }
}

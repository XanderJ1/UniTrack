package com.bash.unitrack.service;

import com.bash.unitrack.authentication.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TokenService {

    private final JwtDecoder jwtDecoder;

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder){
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generate(CustomUserDetails user){

        Instant now = Instant.now();

        List<String> scope = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(user.getUsername())
                .claim("roles", scope)
                .claim("purpose", "authentication")
                .claim("user_id", user.getId())
                .expiresAt(now.plusSeconds(900))
                .build();
        System.out.println(claimsSet.getClaims());
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public String generateVerificationToken(CustomUserDetails user){

        Instant now = Instant.now();

        List<String> scope = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(user.getUsername())
                .claim("roles", scope)
                .claim("purpose", "emailVerification")
                .claim("user_id", user.getId())
                .expiresAt(now.plusSeconds(5000))
                .build();
        System.out.println(claimsSet.getClaims());
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public String generatePasswordToken(CustomUserDetails user){

        Instant now = Instant.now();

        List<String> scope = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(user.getUsername())
                .claim("roles", scope)
                .claim("purpose", "passwordReset")
                .claim("user_id", user.getId())
                .claim("isUsed", false)
                .expiresAt(now.plusSeconds(5000))
                .build();
        System.out.println(claimsSet.getClaims());
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}

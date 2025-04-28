package com.comission.comission.auth;

import com.comission.comission.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {

    private final String secretKey;
    private final UserServiceImpl userServiceImpl;

    public JWTService(UserServiceImpl userServiceImpl)
    {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
            this.userServiceImpl=userServiceImpl;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String username)
    {
        UserDetails user = userServiceImpl.loadUserByUsername(username);
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles",roles);

        return Jwts.builder()
                .header()
                .add("typ","JWT")
                .and()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+3600000))
                .and()
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(String username)
    {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .header()
                .add("typ","JWT")
                .and()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+2592000000L))
                .and()
                .signWith(getKey())
                .compact();
    }

    public SecretKey getKey()
    {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String extractUsername(String token)
    {
        return parseClaims(token,Claims::getSubject);
    }

    private <T> T parseClaims(String token, Function<Claims, T> claimResolver)
    {
        Claims claims = parseAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims parseAllClaims(String token)
    {
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails user)
    {
        String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token)
    {
        return parseClaims(token, Claims::getExpiration);
    }
}

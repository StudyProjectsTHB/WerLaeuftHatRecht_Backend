package com.example.mux.security;

import com.example.mux.user.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuer("CodeJava")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }


    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            System.err.println("JWT expired" + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("Token is null, empty or only whitespace" + ex.getMessage());
        } catch (MalformedJwtException ex) {
            System.err.println("JWT is invalid" + ex);
        } catch (UnsupportedJwtException ex) {
            System.err.println("JWT is not supported" + ex);
        } catch (SignatureException ex) {
            System.err.println("Signature validation failed");
        }

        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}

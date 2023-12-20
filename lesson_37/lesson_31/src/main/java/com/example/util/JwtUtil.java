package com.example.util;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.exp.AppForbiddenException;
import com.example.exp.TokenNotValidException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.Date;

public class JwtUtil {
    private static final String secretKey = "topsecretKey!123";
    private static final int tokenLiveTime = 1000 * 3600 * 24; // 1-day

    public static String encode(Integer profileId, ProfileRole role) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS512, secretKey);

        jwtBuilder.claim("id", profileId);
        jwtBuilder.claim("role", role);

        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (tokenLiveTime)));
        jwtBuilder.setIssuer("Mazgi");

        return jwtBuilder.compact();
    }

    public static JwtDTO decode(String token) {
        JwtParser jwtParser = Jwts.parser();
        jwtParser.setSigningKey(secretKey);

        Jws<Claims> jws = jwtParser.parseClaimsJws(token);

        Claims claims = jws.getBody();

        Integer id = (Integer) claims.get("id");

        String role = (String) claims.get("role");
        ProfileRole profileRole = ProfileRole.valueOf(role);

        return new JwtDTO(id, profileRole);
    }

    public static Integer decodeForEmailVerification(String token) {
        JwtParser jwtParser = Jwts.parser();
        jwtParser.setSigningKey(secretKey);

        Jws<Claims> jws = jwtParser.parseClaimsJws(token);

        Claims claims = jws.getBody();

        Integer id = (Integer) claims.get("id");
        return id;
    }

    public static Integer getIdFromHeader(HttpServletRequest request) {
        try {
            return (Integer) request.getAttribute("id");
        } catch (RuntimeException e) {
            throw new TokenNotValidException("Not Authorized");
        }
    }

    public static JwtDTO getJwtDTO(HttpServletRequest request) {
        try {
            Integer id = (Integer) request.getAttribute("id");
            ProfileRole role = (ProfileRole) request.getAttribute("role");

            return new JwtDTO(id, role);
        } catch (RuntimeException e) {
            throw new TokenNotValidException("Not Authorized");
        }
    }

    public static String encode(Integer profileId) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS512, secretKey);

        jwtBuilder.claim("id", profileId);
        int tokenLiveTime = 1000 * 3600 * 1;
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (tokenLiveTime)));
        jwtBuilder.setIssuer("Mazgi");

        return jwtBuilder.compact();
    }

    public static Integer getIdFromHeader(HttpServletRequest request, ProfileRole role) {
        Integer id = (Integer) request.getAttribute("id");
        ProfileRole jwtRole = (ProfileRole) request.getAttribute("role");
        if (!jwtRole.equals(role)) {
            throw new AppForbiddenException("Method Not Allowed");
        }
        return id;
    }


   /* public static JwtDTO decode(String token) {
        try {
            JwtParser jwtParser = Jwts.parser();
            jwtParser.setSigningKey(secretKey);

            Jws<Claims> jws = jwtParser.parseClaimsJws(token);

            Claims claims = jws.getBody();

            Integer id = (Integer) claims.get("id");

            String role = (String) claims.get("role");
            ProfileRole profileRole = ProfileRole.valueOf(role);

            return new JwtDTO(id, profileRole);

        } catch (SignatureException e) {
            // so("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            //  logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            // logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            // logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            //
        }
        return null;
    }*/


  /*  public static String getTokenFromHeader(String header) {
        if (!header.startsWith("Bearer ")) {
            throw new RuntimeException("Token Not exists");
        }
        String[] array = header.split(" ");
        if (array.length != 2) {
            throw new RuntimeException("Token Not exists");
        }
        return array[1].trim();
    }*/


}

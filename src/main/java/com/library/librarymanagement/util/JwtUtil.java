package com.library.librarymanagement.util;

import com.library.librarymanagement.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JwtUtil {

    public static UserDto.UserCredential getCredentialJwt(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String token = httpRequest.getHeader("Authorization").split(" ")[1];

        Claims claims = Jwts.parser().verifyWith(getSignedKey()).build().parseSignedClaims(token).getPayload();
        return UserDto.UserCredential.builder()
                .fullName(claims.getSubject())
                .email(claims.get("email", String.class))
                .roleName(claims.get("role", String.class))
                .userId(claims.get("userId", String.class))
                .build();
    }

    public static String JwtGenerator(UserDto.UserCredential credential) {
        return Jwts
                .builder()
                .subject(credential.getFullName())
                .claim("role", credential.getRoleName())
                .claim("userId", credential.getUserId())
                .claim("email", credential.getEmail())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSignedKey()).compact();
    }

    private static SecretKey getSignedKey() {
        byte[] keyBytes = Base64.getDecoder().decode("0393e944ee8108bb66fc9fa4f99f9c862481e9e0519e18232ba61b0767eee8c6".getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }
}

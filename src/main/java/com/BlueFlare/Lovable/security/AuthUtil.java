package com.BlueFlare.Lovable.security;


import com.BlueFlare.Lovable.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AuthUtil {

    @Value("$(jwt-secret-key")
    private String jwtSecretKey;


    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String  generateAccessToken(User user){
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*10))
                .signWith(getSecretKey())
                .compact();
    }
}

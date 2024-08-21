package com.example.thucbashop.components;

import com.example.thucbashop.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private int expiration;//luu vao bien moi truong
    //ham tao token
    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user) throws Exception{
        Map<String,Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        try{
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {//can logger
           throw new InvalidParameterException("cannot create jwt token ,error: " + e.getMessage());

        }
    }

    private Key getSignInKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey);
//        Keys.hmacShaKeyFor(Decoders.BASE64.decode(scrobwoI/xAgwDqhMTPXqBT2DmaDPcbYWJlmhdo+CQk=))
        return Keys.hmacShaKeyFor(bytes);
    }
    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            // Handle JWT parsing exception
            e.printStackTrace();
            return null;
        }
    }
    public  <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    //check hethan
    public boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaim(token,Claims::getExpiration);
        return expirationDate.before(new Date());
    }
    //chich xuat phoone number tu token
    public String extractPhoneNumber(String token){
        return extractClaim(token,Claims::getSubject);
    }
    //token con han k user name co trung hay khong
    public boolean validateToken(String token, UserDetails userDetails){
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }
}

package com.example.chatserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Signature;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String secretkey;
    private final int expiration;
    private Key SECRET_KEY;

    public JwtTokenProvider(@Value("${jwt.secretKey}")String secretkey,  @Value("${jwt.expiration}")int expiration) {
        this.secretkey = secretkey;
        this.expiration = expiration;
        this.SECRET_KEY = new SecretKeySpec(
                java.util.Base64.getDecoder().decode(secretkey),  //시크릿키 디코딩 시킴 -> chatserversecretaccesstokenchatserversecretaccesstokenchatserversecretaccesstoken
                SignatureAlgorithm.HS512.getJcaName()); //암호화시킴
    }

    public String createToken(String email, String role){
       Claims claims = Jwts.claims().setSubject(email); //claims는 payload라고 생가하면됨, subject로 키값 설정
       claims.put("role", role);
       Date now = new Date();
       String token = Jwts.builder()
               .setClaims(claims)
               .setIssuedAt(now) //발행시간 설정
               .setExpiration(new Date(now.getTime()+expiration*60*1000L)) //만료일자 설정, 현재 시간에 + 토큰 만료 시간 ---> 밀리초 단위
               .signWith(SECRET_KEY) //서명
               .compact();
       return token;
    }
}

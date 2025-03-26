package com.example.chatserver.chat.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class StompHandler implements ChannelInterceptor {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        //사용자 요청은 message에 담겨있음 -> 여기서 토큰 꺼내볼거임

        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT == accessor.getCommand()) {
            System.out.println("connect 요청시 토큰 유효성 검증");
            String bearerToken = accessor.getFirstNativeHeader("Authorization");

            String token = bearerToken.substring(7);

            //토큰 검증 및 claims 추출
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("토큰 검증 완료");
        }

        return message;
    }


}

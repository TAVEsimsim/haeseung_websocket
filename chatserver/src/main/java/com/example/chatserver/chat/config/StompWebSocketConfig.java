package com.example.chatserver.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //Stomp다
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    public StompWebSocketConfig(StompHandler stompHandler) {
        this.stompHandler = stompHandler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOrigins("http://localhost:3000")
                // ws:// 아닌 http:// 엔드포인트를 사용할 수 있게 해주는 sockJs라이브러리를 통한 요청을 허용하는 설정
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //메시지를 발행할 때는 ex. /publish/1 이런 룰을 가지고 발행
        // /publish로 시작하는 url 패턴으로 메시지가 발행되면 @Controller 객체의 @MessageMapping 메서드로 라우팅
        registry.setApplicationDestinationPrefixes("/publish");

        registry.enableSimpleBroker("/topic"); // /topic/1 형태로 메시지를 수신(subscribe)해야 함을 설정
    }


    //웹소켓 요청(connect, subscribe, disconnect)등의 요청시에는 Http Header 등 http 메시지를 넣어올 수 있고,
    //이를 interceptor를 통해 가로채 토큰 등을 검증할 수 있음
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler); //사용자의 요청을 필터를 지나고 컨피그 가기 전에 낚아챔
    }
}

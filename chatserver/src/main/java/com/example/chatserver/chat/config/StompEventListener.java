package com.example.chatserver.chat.config;


import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//스프링과 stomp는 기본적으로 세션관리를 자동(내부적)으로 관리
//연결/해제 이벤트를 기록, 연결된 세션 수를 실시간으로 확인할 목저으로 이벤트 리스너를 생성 => 로그, 디버깅 목적
@Component
public class StompEventListener {

    private final Set<String> seesions = ConcurrentHashMap.newKeySet();

    @EventListener
    public void connectHandle(SessionConnectedEvent event) { //커넥션 요청이 발생했을 때 이 이벤트 리스너가 실행됨

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        seesions.add(accessor.getSessionId());

        System.out.println("connect session ID" + accessor.getSessionId());
        System.out.println("total session " + seesions.size());

    }

    @EventListener
    public void disconnectHandle(SessionDisconnectEvent event) { //커넥션 요청이 발생했을 때 이 이벤트 리스너가 실행됨

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        seesions.remove(accessor.getSessionId());

        System.out.println("connect session ID" + accessor.getSessionId());
        System.out.println("total session " + seesions.size());
    }
}

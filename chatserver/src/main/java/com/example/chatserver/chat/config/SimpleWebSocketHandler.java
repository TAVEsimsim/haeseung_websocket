package com.example.chatserver.chat.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


//connect으로 웹소켓 연결 요청이 들어왔을 이를 처리할 클래스
@Component
public class SimpleWebSocketHandler extends TextWebSocketHandler {

    //연결된 세션 관리 : 스레드 safe한 set 사용
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Connected : " + session.getId());
    }

    //메세지가 들어왔을 어떻게 할것인가?
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("received message : " + payload );

        //모든 세션에게 내가 받은 메시지를 보내겠다
        for(WebSocketSession s : sessions){
            if(s.isOpen()){ //현재 받아줄 있다면 메시지를 보내겠다.
                s.sendMessage(new TextMessage(payload)); //TextMessage 형태로 페이로드 보내주겠다.
            }
        }
    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("disconnected!");
    }


}

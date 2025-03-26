package com.example.chatserver.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {

    @MessageMapping("/{roomId}") //클라이언트에서 특정 publish/roomId 형태로 메시지를 발행시 MessageMapping 수신
    @SendTo("/topic/{roomId}") //해당 roomId에 메시지를 발행하여 구독중인 클라이언트에게 메시지를 전송
    // @DestinationVariable은 @MessageMapping 어노테이션으로 정의된 Websocket controller 내에서만 사용
    public String sendMessage(@DestinationVariable Long roomId, String message) {
        System.out.println(message);

        return message; //SendTo 자리로 메시지 발행
    }
}

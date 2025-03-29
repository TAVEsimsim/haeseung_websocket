package com.example.chatserver.chat.controller;

import com.example.chatserver.chat.dto.ChatMessageReqDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {
    private final SimpMessagingTemplate messageTemplate;

    public StompController(SimpMessagingTemplate messageTemplate) {
        this.messageTemplate = messageTemplate;
    }
//    // 방법 1. MessageMapping(수신)과 SendTo 한번에 처리
//    @MessageMapping("/{roomId}") //클라이언트에서 특정 publish/roomId 형태로 메시지 발행시 MessageMapping 수신
//    @SendTo("/topic/{roomId}") //해당 roomId에 메시지 발행하여 구독중인 클라이언트에게 전송
//    //DestinationVariable : @MessageMapping 어노테이션으로 정의된 websocket controller 내에서만 사용
//    public String sendMessage(@DestinationVariable Long roomId, String message) {
//        System.out.println(message);
//
//        return message;
//    }

    //방법 2. MessageMapping어노테이션만 활용
    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageReqDto chatMessageReqDto) {
        System.out.println(chatMessageReqDto.getMessage());
        messageTemplate.convertAndSend("/topic/"+roomId, chatMessageReqDto);
    }
}
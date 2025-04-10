package com.example.chatserver.chat.service;

import com.example.chatserver.chat.domain.ChatMessage;
import com.example.chatserver.chat.dto.ChatMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPubSubService implements MessageListener {

    private final StringRedisTemplate stringRedisTemplate;
    private final SimpMessagingTemplate messageTemplate;


    public RedisPubSubService(@Qualifier("chatPubSub") StringRedisTemplate stringRedisTemplate, SimpMessagingTemplate messageTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.messageTemplate = messageTemplate;
    }

    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }

    @Override
//    Pattern에는 topic의 이름의 패턴이 담겨있고, 이 패턴을 기반으로 다이나믹한 코딩
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ChatMessageDto chatMessagedto = objectMapper.readValue(payload, ChatMessageDto.class);
            messageTemplate.convertAndSend("/topic/"+chatMessagedto.getRoomId(), chatMessagedto);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}

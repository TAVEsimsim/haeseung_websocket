package com.example.chatserver.common.configs;

import com.example.chatserver.chat.service.RedisPubSubService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private Integer port;

//    연결 기본 객체 ( 레디스 연결을 위한 기본 객체)
    @Bean
    @Qualifier("chatPubSub")
    public RedisConnectionFactory chatPubSubFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
//        redis pub/sub에서는 특정 데이터베이스에 의존적이지 않음.
//        configuration.setDatabase(0);

        return new LettuceConnectionFactory(configuration);
    }


//    publish 객체
    @Bean
    @Qualifier("chatPubSub")
//    일반적으로는 RedisTemplate<키 데이터 타입, 벨류데이터 타입>을 씀
    public StringRedisTemplate stringRedisTemplate(@Qualifier("chatPubSub") RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

//    subscribe 객체
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            @Qualifier("chatPubSub") RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter messageListenerAdapter
            ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        //리슨 하고 나서 처리 해주는 리스너 넣어야함 - 위임해줌
        container.addMessageListener(messageListenerAdapter, new PatternTopic("chat"));
        return container;
    }

    //    redis에서 수신된 메시지를 처리하는 객체 생성
    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisPubSubService redisPubSubService){
//        RedisPubSubService의 특정 메서드가 수신된 메시지를 처리할수 있도록 지정
        return new MessageListenerAdapter(redisPubSubService, "onMessage");

    }

}

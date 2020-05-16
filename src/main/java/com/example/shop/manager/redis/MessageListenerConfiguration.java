package com.example.shop.manager.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

@Configuration
public class MessageListenerConfiguration {

    @Value("${spring.redis.listen-pattern}")
    private String pattern;

    @Autowired
    private TopicMessageListener topicMessageListener;

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory redisConnection) {
        // 获取容器
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 建立连接
        container.setConnectionFactory(redisConnection);
        // 添加监听器配置
        Topic topic = new PatternTopic(pattern);
        // 添加监听器
        container.addMessageListener(topicMessageListener, topic);
        return container;
    }
}

package com.ceratipa.vertexregistry.spring.config;

import com.ceratipa.vertexregistry.core.domain.messaging.VertexRegistryChannelListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class RedisConfig {
    @Value("${redis.channel.vertex-registry-events}")
    private final String vertexRegistryEventsChannel;
    private final RedisConnectionFactory redisConnectionFactory;
    private final VertexRegistryChannelListener vertexRegistryChannelListener;

    @Bean
    MessageListenerAdapter notificationsMessageListener() {
        return new MessageListenerAdapter(vertexRegistryChannelListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(notificationsMessageListener(), new ChannelTopic(vertexRegistryEventsChannel));
        return container;
    }
}

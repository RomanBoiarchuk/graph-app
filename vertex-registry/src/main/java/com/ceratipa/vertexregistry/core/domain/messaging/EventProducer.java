package com.ceratipa.vertexregistry.core.domain.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventProducer {
    private static final UUID GRAPH_ID = UUID.fromString("8e201920-c77d-4408-b21a-8219db63d569");
    @Value("${redis.channel.vertex-registry-events}")
    private final String vertexRegistryEventsChannel;
    @Value("${kafka.topic.vertex-registry-events}")
    private final String topic;
    private final RedisTemplate<String, String> redisTemplate;
    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @SneakyThrows
    public void convertAndSend(Event event) {
        String message = objectMapper.writeValueAsString(event);
        kafkaTemplate.send(topic, GRAPH_ID.toString(), event);
        redisTemplate.convertAndSend(vertexRegistryEventsChannel, message);
    }
}

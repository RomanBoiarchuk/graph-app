package com.ceratipa.vertexregistry.core.domain.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class VertexRegistryChannelListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private Set<VertexRegistryEventsObserver> observers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    @SneakyThrows
    public void onMessage(Message message, byte[] pattern) {
        Event event = objectMapper.readValue(message.toString(), Event.class);
        observers.forEach(observer -> observer.onNext(event));
    }

    public void addObserver(VertexRegistryEventsObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(VertexRegistryEventsObserver observer) {
        observers.remove(observer);
    }
}

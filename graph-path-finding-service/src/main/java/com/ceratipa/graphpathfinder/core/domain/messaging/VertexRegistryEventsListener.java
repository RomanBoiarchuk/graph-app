package com.ceratipa.graphpathfinder.core.domain.messaging;

import com.ceratipa.graphpathfinder.core.domain.entity.Edge;
import com.ceratipa.graphpathfinder.core.domain.service.EdgeService;
import com.ceratipa.graphpathfinder.core.domain.service.VertexService;
import com.ceratipa.vertexregistry.core.domain.messaging.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VertexRegistryEventsListener {
    private static final Logger LOG = LoggerFactory.getLogger(VertexRegistryEventsListener.class);
    private final VertexService vertexService;
    private final EdgeService edgeService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${kafka.topic.vertex-registry-events}",
            groupId = "${kafka.consumer-group}",
            properties = {
                    "key.deserializer:org.apache.kafka.common.serialization.StringDeserializer",
                    "value.deserializer:org.springframework.kafka.support.serializer.JsonDeserializer"
            }
    )
    @SneakyThrows
    public void consume(ConsumerRecord<String, Event> message) {
        var event = message.value();
        LOG.info("Processing event {}", event);
        ObjectNode payload = objectMapper.readValue(event.payload(), ObjectNode.class);
        switch (event.eventType()) {
            case "VertexAdded" -> createVertex(payload);
            case "VertexRemoved" -> removeVertex(payload);
            case "EdgeAdded" -> createEdge(payload);
            case "EdgeRemoved" -> removeEdge(payload);
            default -> LOG.info("Unknown type {}. Skipping event {}", event.eventType(), event);
        }
    }

    private void createVertex(ObjectNode payload) {
        UUID vertexId = extractUUID(payload, "vertexId");
        vertexService.addVertex(vertexId);
    }

    private void removeVertex(ObjectNode payload) {
        UUID vertexId = extractUUID(payload, "vertexId");
        vertexService.deleteById(vertexId);
    }

    private void createEdge(ObjectNode payload) {
        UUID edgeId = extractUUID(payload, "edgeId");
        BigDecimal weight = BigDecimal.valueOf(payload.get("weight").doubleValue());
        UUID from = extractUUID(payload, "from");
        UUID to = extractUUID(payload, "to");

        Edge edge = new Edge();
        edge.setId(edgeId);
        edge.setWeight(weight);
        edge.setFromVertexId(from);
        edge.setToVertexId(to);

        edgeService.save(edge);
    }

    private void removeEdge(ObjectNode payload) {
        UUID edgeId = extractUUID(payload, "edgeId");
        edgeService.deleteById(edgeId);
    }

    private UUID extractUUID(ObjectNode payload, String fieldName) {
        return UUID.fromString(payload.get(fieldName).asText());
    }
}

package com.ceratipa.vertexregistry.web.query;

import com.ceratipa.vertexregistry.core.domain.messaging.VertexRegistryChannelListener;
import com.ceratipa.vertexregistry.core.domain.messaging.VertexRegistryEventsObserver;
import com.ceratipa.vertexregistry.core.domain.service.EdgeService;
import com.ceratipa.vertexregistry.core.domain.service.VertexService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class GraphSubscriptionController {
    private final VertexRegistryChannelListener vertexRegistryChannelListener;
    private final VertexService vertexService;
    private final EdgeService edgeService;
    private final ObjectMapper objectMapper;

    @SubscriptionMapping
    public Publisher<Map<String, Object>> graph() {
        return Flux.create(sink -> {
            var vertexes = new ArrayList<>(
                    vertexService.findAll().stream()
                            .map(v -> new VertexDto(v.getId(), v.getName()))
                            .toList()
            );
            var edges = new ArrayList<>(
                    edgeService.findAll().stream()
                            .map(e -> new EdgeDto(e.getId(), e.getWeight().doubleValue(), e.getFrom().getId(), e.getTo().getId()))
                            .toList()
            );

            sink.next(Map.of("vertexes", vertexes, "edges", edges));

            VertexRegistryEventsObserver observer = event -> {
                switch (event.getEventType()) {
                    case "VertexAdded" -> vertexes.add(createVertex(event.getPayload()));
                    case "VertexRemoved" -> vertexes.removeIf(v -> v.id().equals(extractVertexId(event.getPayload())));
                    case "EdgeAdded" -> edges.add(createEdge(event.getPayload()));
                    case "EdgeRemoved" -> edges.removeIf(e -> e.id().equals(extractEdgeId(event.getPayload())));
                    default -> sink.complete();
                }
                sink.next(Map.of("vertexes", vertexes, "edges", edges));
            };

            vertexRegistryChannelListener.addObserver(observer);
            sink.onDispose(() -> vertexRegistryChannelListener.removeObserver(observer));
        });
    }

    @SneakyThrows
    private VertexDto createVertex(String payload) {
        ObjectNode object = objectMapper.readValue(payload, ObjectNode.class);
        UUID id = UUID.fromString(object.get("vertexId").asText());
        String name = object.get("name").asText();
        return new VertexDto(id, name);
    }

    @SneakyThrows
    private EdgeDto createEdge(String payload) {
        ObjectNode object = objectMapper.readValue(payload, ObjectNode.class);
        UUID id = UUID.fromString(object.get("edgeId").asText());
        double weight = object.get("weight").doubleValue();
        UUID from = UUID.fromString(object.get("from").asText());
        UUID to = UUID.fromString(object.get("to").asText());
        return new EdgeDto(id, weight, from, to);
    }

    @SneakyThrows
    private UUID extractVertexId(String payload) {
        ObjectNode object = objectMapper.readValue(payload, ObjectNode.class);
        return UUID.fromString(object.get("vertexId").asText());
    }

    @SneakyThrows
    private UUID extractEdgeId(String payload) {
        ObjectNode object = objectMapper.readValue(payload, ObjectNode.class);
        return UUID.fromString(object.get("edgeId").asText());
    }
}

record VertexDto(UUID id, String name) {
}

record EdgeDto(UUID id, double weight, UUID from, UUID to) {
}

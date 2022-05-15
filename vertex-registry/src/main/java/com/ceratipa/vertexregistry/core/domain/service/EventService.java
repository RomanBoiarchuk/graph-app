package com.ceratipa.vertexregistry.core.domain.service;

import com.ceratipa.vertexregistry.core.domain.command.Command;
import com.ceratipa.vertexregistry.core.domain.command.edge.AddEdge;
import com.ceratipa.vertexregistry.core.domain.command.edge.RemoveEdge;
import com.ceratipa.vertexregistry.core.domain.command.vertex.AddVertex;
import com.ceratipa.vertexregistry.core.domain.command.vertex.RemoveVertex;
import com.ceratipa.vertexregistry.core.domain.entity.Edge;
import com.ceratipa.vertexregistry.core.domain.entity.Vertex;
import com.ceratipa.vertexregistry.core.domain.messaging.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EventService {
    private final VertexService vertexService;
    private final EdgeService edgeService;
    private final ObjectMapper objectMapper;

    public List<Event> process(AddVertex command) {
        Vertex vertex = new Vertex();
        vertex.setId(command.getVertexId());
        vertex.setName(command.getName());
        vertexService.save(vertex);
        return List.of(toEvent(command));
    }

    public List<Event> process(RemoveVertex command) {
        var edges = edgeService.findAdjacentEdges(command.vertexId());
        edges.forEach(e -> edgeService.deleteById(e.getId()));
        vertexService.deleteById(command.vertexId());
        return Stream.concat(
                        edges.stream().map(edge -> new RemoveEdge(edge.getId())),
                        Stream.of(command)
                )
                .map(this::toEvent)
                .toList();
    }

    public List<Event> process(AddEdge command) {
        if (edgeService.existsByVertexIds(command.getFrom(), command.getTo())) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }

        Vertex from = vertexService.findById(command.getFrom()).orElseThrow();
        Vertex to = vertexService.findById(command.getTo()).orElseThrow();

        Edge edge = new Edge();
        edge.setId(command.getEdgeId());
        edge.setWeight(command.getWeight());
        edge.setFrom(from);
        edge.setTo(to);

        edgeService.save(edge);
        return List.of(toEvent(command));
    }

    public List<Event> process(RemoveEdge command) {
        edgeService.deleteById(command.edgeId());
        return List.of(toEvent(command));
    }

    @SneakyThrows
    private Event toEvent(Command command) {
        return Event.builder()
                .id(UUID.randomUUID())
                .eventType(command.getEventType())
                .payload(objectMapper.writeValueAsString(command))
                .timestamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")))
                .build();
    }
}

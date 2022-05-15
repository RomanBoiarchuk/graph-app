package com.ceratipa.vertexregistry.core.domain.handler;

import com.ceratipa.vertexregistry.core.domain.command.edge.AddEdge;
import com.ceratipa.vertexregistry.core.domain.command.edge.RemoveEdge;
import com.ceratipa.vertexregistry.core.domain.command.vertex.AddVertex;
import com.ceratipa.vertexregistry.core.domain.command.vertex.RemoveVertex;
import com.ceratipa.vertexregistry.core.domain.messaging.Event;
import com.ceratipa.vertexregistry.core.domain.messaging.EventProducer;
import com.ceratipa.vertexregistry.core.domain.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class GraphCommandHandler {
    private final EventService eventService;
    private final EventProducer eventProducer;

    public UUID on(AddVertex command) {
        command.setVertexId(UUID.randomUUID());
        List<Event> events = eventService.process(command);
        events.forEach(eventProducer::convertAndSend);
        return command.getVertexId();
    }

    public void on(RemoveVertex command) {
        List<Event> events = eventService.process(command);
        events.forEach(eventProducer::convertAndSend);
    }

    public UUID on(AddEdge command) {
        command.setEdgeId(UUID.randomUUID());
        List<Event> events = eventService.process(command);
        events.forEach(eventProducer::convertAndSend);
        return command.getEdgeId();
    }

    public void on(RemoveEdge command) {
        List<Event> events = eventService.process(command);
        events.forEach(eventProducer::convertAndSend);
    }
}

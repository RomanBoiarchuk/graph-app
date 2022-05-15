package com.ceratipa.vertexregistry.web.command;

import com.ceratipa.vertexregistry.core.domain.command.edge.AddEdge;
import com.ceratipa.vertexregistry.core.domain.command.edge.RemoveEdge;
import com.ceratipa.vertexregistry.core.domain.handler.GraphCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EdgeController {
    private final GraphCommandHandler graphCommandHandler;

    @PostMapping("add-edge")
    public UUID addEdge(@Valid AddEdge command) {
        return graphCommandHandler.on(command);
    }

    @PostMapping("remove-edge")
    public void removeEdge(@Valid RemoveEdge command) {
        graphCommandHandler.on(command);
    }
}

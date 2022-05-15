package com.ceratipa.vertexregistry.web.command;

import com.ceratipa.vertexregistry.core.domain.command.vertex.AddVertex;
import com.ceratipa.vertexregistry.core.domain.command.vertex.RemoveVertex;
import com.ceratipa.vertexregistry.core.domain.handler.GraphCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VertexController {
    private final GraphCommandHandler graphCommandHandler;

    @PostMapping("/add-vertex")
    public UUID addVertex(@Valid AddVertex command) {
        return graphCommandHandler.on(command);
    }

    @PostMapping("remove-vertex")
    public void removeVertex(@Valid RemoveVertex command) {
        graphCommandHandler.on(command);
    }
}

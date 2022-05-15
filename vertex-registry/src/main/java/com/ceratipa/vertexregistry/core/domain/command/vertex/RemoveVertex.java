package com.ceratipa.vertexregistry.core.domain.command.vertex;

import com.ceratipa.vertexregistry.core.domain.command.Command;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record RemoveVertex(@NotNull(message = "Vertex id can't be null.") UUID vertexId) implements Command {
    @Override
    @JsonIgnore
    public String getEventType() {
        return "VertexRemoved";
    }
}

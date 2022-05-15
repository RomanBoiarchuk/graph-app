package com.ceratipa.vertexregistry.core.domain.command.edge;

import com.ceratipa.vertexregistry.core.domain.command.Command;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record RemoveEdge(@NotNull(message = "Edge id can't be null.") UUID edgeId) implements Command {
    @Override
    @JsonIgnore
    public String getEventType() {
        return "EdgeRemoved";
    }
}

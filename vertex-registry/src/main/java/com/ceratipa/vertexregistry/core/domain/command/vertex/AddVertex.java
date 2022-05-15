package com.ceratipa.vertexregistry.core.domain.command.vertex;

import com.ceratipa.vertexregistry.core.domain.command.Command;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class AddVertex implements Command {

    private UUID vertexId;

    @NotNull(message = "Name can't be null.")
    private String name;

    @Override
    @JsonIgnore
    public String getEventType() {
        return "VertexAdded";
    }
}

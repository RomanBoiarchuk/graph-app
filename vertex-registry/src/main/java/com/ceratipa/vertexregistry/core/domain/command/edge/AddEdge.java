package com.ceratipa.vertexregistry.core.domain.command.edge;

import com.ceratipa.vertexregistry.core.domain.command.Command;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AddEdge implements Command {

    private UUID edgeId;

    @DecimalMin(value = "0")
    @Digits(integer = 19, fraction = 2)
    @NotNull(message = "Weight can't be null.")
    private BigDecimal weight;

    @NotNull(message = "Vertex id [from] can't be null.")
    private UUID from;

    @NotNull(message = "Vertex id [to] can't be null.")
    private UUID to;

    @Override
    @JsonIgnore
    public String getEventType() {
        return "EdgeAdded";
    }
}

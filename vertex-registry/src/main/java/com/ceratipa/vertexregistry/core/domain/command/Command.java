package com.ceratipa.vertexregistry.core.domain.command;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Command {
    @JsonIgnore
    String getEventType();
}

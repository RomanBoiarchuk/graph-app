package com.ceratipa.vertexregistry.core.domain.messaging;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
public class Event {
    private UUID id;
    private String eventType;
    private String payload;
    private ZonedDateTime timestamp;
}

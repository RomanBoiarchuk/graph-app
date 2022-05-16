package com.ceratipa.graphpathfinder.core.domain.messaging;

import java.time.ZonedDateTime;
import java.util.UUID;

public record Event(UUID id, String eventType, String payload, ZonedDateTime timestamp) {
}

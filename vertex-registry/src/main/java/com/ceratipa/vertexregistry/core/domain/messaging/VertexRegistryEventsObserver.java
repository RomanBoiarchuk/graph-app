package com.ceratipa.vertexregistry.core.domain.messaging;

public interface VertexRegistryEventsObserver {
    void onNext(Event event);
}

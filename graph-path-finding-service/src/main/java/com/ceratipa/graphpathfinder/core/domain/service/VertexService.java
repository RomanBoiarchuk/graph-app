package com.ceratipa.graphpathfinder.core.domain.service;

import com.ceratipa.graphpathfinder.core.domain.entity.Vertex;
import com.ceratipa.graphpathfinder.repository.jpa.VertexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VertexService {
    private final VertexRepository vertexRepository;

    public void addVertex(UUID vertexId) {
        Vertex entity = new Vertex();
        entity.setId(vertexId);
        vertexRepository.save(entity);
    }

    public void deleteById(UUID vertexId) {
        vertexRepository.deleteById(vertexId);
    }
}

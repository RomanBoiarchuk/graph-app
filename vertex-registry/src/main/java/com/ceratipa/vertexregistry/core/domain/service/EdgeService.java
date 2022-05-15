package com.ceratipa.vertexregistry.core.domain.service;

import com.ceratipa.vertexregistry.core.domain.entity.Edge;
import com.ceratipa.vertexregistry.repository.jpa.EdgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EdgeService {
    private final EdgeRepository edgeRepository;

    public List<Edge> findAll() {
        return edgeRepository.findAll();
    }

    public void save(Edge edge) {
        edgeRepository.save(edge);
    }

    public List<Edge> findAdjacentEdges(UUID vertexId) {
        return edgeRepository.findByFromIdOrToId(vertexId, vertexId);
    }

    public boolean existsByVertexIds(UUID vertex1, UUID vertex2) {
        return edgeRepository.findByVertexIds(vertex1, vertex2).size() > 0;
    }

    public void deleteById(UUID edgeId) {
        edgeRepository.deleteById(edgeId);
    }
}

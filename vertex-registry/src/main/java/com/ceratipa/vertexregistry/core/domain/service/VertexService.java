package com.ceratipa.vertexregistry.core.domain.service;

import com.ceratipa.vertexregistry.core.domain.entity.Vertex;
import com.ceratipa.vertexregistry.repository.jpa.VertexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VertexService {
    private final VertexRepository vertexRepository;

    public List<Vertex> findAll() {
        return vertexRepository.findAll();
    }

    public void save(Vertex vertex) {
        vertexRepository.save(vertex);
    }

    public Optional<Vertex> findById(UUID id) {
        return vertexRepository.findById(id);
    }

    public void deleteById(UUID vertexId) {
        vertexRepository.deleteById(vertexId);
    }
}

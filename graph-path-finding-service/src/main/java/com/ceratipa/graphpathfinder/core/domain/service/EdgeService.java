package com.ceratipa.graphpathfinder.core.domain.service;

import com.ceratipa.graphpathfinder.core.domain.entity.Edge;
import com.ceratipa.graphpathfinder.repository.jpa.EdgeRepository;
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

    public void deleteById(UUID edgeId) {
        edgeRepository.deleteById(edgeId);
    }
}

package com.ceratipa.graphpathfinder.core.domain.service;

import com.ceratipa.graphpathfinder.core.domain.entity.Edge;
import com.ceratipa.graphpathfinder.core.domain.entity.Vertex;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PathFinder {
    private static final Logger LOG = LoggerFactory.getLogger(PathFinder.class);
    private final VertexService vertexService;
    private final EdgeService edgeService;
    private final ShortestPathCacheService cacheService;
    private final FloydWarshallAlgorithm algorithm;

    public List<UUID> findShortestPath(UUID sourceVertexId, UUID destinationVertexId) {
        return cacheService.retrieveShortestPathMatrix()
                .map(matrix -> {
                    List<UUID> vertexList = cacheService.retrieveVertexList();
                    return algorithm.constructPath(matrix, vertexList, sourceVertexId, destinationVertexId);
                })
                .orElseGet(() -> findPathAndUpdateCache(sourceVertexId, destinationVertexId));
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    private void updateCache() {
        LOG.info("Updating cache...");
        if (cacheService.isCacheUpToDate()) {
            LOG.info("Cache is up to date.");
            return;
        }
        var vertexes = getVertexes();
        var matrix = getShortestPathMatrix(vertexes);
        cacheService.saveShortestPathMatrix(matrix, vertexes);
        LOG.info("Cache updated.");
    }

    private List<UUID> findPathAndUpdateCache(UUID sourceVertexId, UUID destinationVertexId) {
        LOG.info("Cache is outdated. Updating...");
        var vertexes = getVertexes();
        var matrix = getShortestPathMatrix(vertexes);
        cacheService.saveShortestPathMatrix(matrix, vertexes);
        LOG.info("Cache updated.");
        return algorithm.constructPath(matrix, vertexes, sourceVertexId, destinationVertexId);
    }

    private List<List<UUID>> getShortestPathMatrix(List<UUID> vertexes) {
        List<Edge> edges = edgeService.findAll();
        return algorithm.calculateShortestPathMatrix(vertexes, edges);
    }

    private List<UUID> getVertexes() {
        return vertexService.findAll().stream().map(Vertex::getId).toList();
    }
}

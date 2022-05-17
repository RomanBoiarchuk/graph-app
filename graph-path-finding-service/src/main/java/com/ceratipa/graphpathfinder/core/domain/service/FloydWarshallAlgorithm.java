package com.ceratipa.graphpathfinder.core.domain.service;

import com.ceratipa.graphpathfinder.core.domain.entity.Edge;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FloydWarshallAlgorithm {

    public List<List<UUID>> calculateShortestPathMatrix(List<UUID> vertexes, List<Edge> edges) {
        int size = vertexes.size();
        Map<UUID, Integer> vertexIndexById = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            vertexIndexById.put(vertexes.get(i), i);
        }

        List<List<Double>> dist = new ArrayList<>(size);
        List<List<UUID>> next = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            List<Double> distSubList = new ArrayList<>(size);
            List<UUID> nextSubList = new ArrayList<>(size);
            for (int j = 0; j < size; j++) {
                distSubList.add(Double.MAX_VALUE);
                nextSubList.add(null);
            }
            dist.add(distSubList);
            next.add(nextSubList);
        }

        edges.forEach(edge -> {
            int fromIndex = vertexIndexById.get(edge.getFromVertexId());
            int toIndex = vertexIndexById.get(edge.getToVertexId());

            dist.get(fromIndex).set(toIndex, edge.getWeight().doubleValue());
            dist.get(toIndex).set(fromIndex, edge.getWeight().doubleValue());

            next.get(fromIndex).set(toIndex, edge.getToVertexId());
            next.get(toIndex).set(fromIndex, edge.getFromVertexId());
        });

        for (int i = 0; i < size; i++) {
            dist.get(i).set(i, 0d);
            next.get(i).set(i, vertexes.get(i));
        }

        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (dist.get(i).get(j) > dist.get(i).get(k) + dist.get(k).get(j)) {
                        dist.get(i).set(j, dist.get(i).get(k) + dist.get(k).get(j));
                        next.get(i).set(j, next.get(i).get(k));
                    }
                }
            }
        }
        return next;
    }

    public List<UUID> constructPath(List<List<UUID>> matrix, List<UUID> vertexes, UUID sourceVertexId, UUID destinationVertexId) {
        int size = vertexes.size();
        Map<UUID, Integer> vertexIndexById = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            vertexIndexById.put(vertexes.get(i), i);
        }

        var sourceIndex = vertexIndexById.get(sourceVertexId);
        var destinationIndex = vertexIndexById.get(destinationVertexId);

        List<UUID> path = new ArrayList<>();

        if (matrix.get(sourceIndex).get(destinationIndex) == null) {
            return path;
        }

        path.add(sourceVertexId);

        while (!sourceIndex.equals(destinationIndex)) {
            sourceVertexId = matrix.get(sourceIndex).get(destinationIndex);
            sourceIndex = vertexIndexById.get(sourceVertexId);
            path.add(sourceVertexId);
        }

        return path;
    }
}

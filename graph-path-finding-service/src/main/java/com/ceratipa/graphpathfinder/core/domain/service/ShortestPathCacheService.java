package com.ceratipa.graphpathfinder.core.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShortestPathCacheService {
    private static final String LAST_GRAPH_INTERACTION_TIMESTAMP_KEY = "last_graph_interaction_timestamp";
    private static final String CACHE_UPDATE_TIMESTAMP_KEY = "cache_update_timestamp";
    private static final String SHORTEST_PATH_MATRIX_KEY = "shortest_path_matrix";
    private static final String VERTEX_LIST_KEY = "vertex_list_matrix";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void updateGraphInteractionTimestamp() {
        long timestamp = System.currentTimeMillis();
        redisTemplate.opsForValue().set(LAST_GRAPH_INTERACTION_TIMESTAMP_KEY, String.valueOf(timestamp));
    }

    @SneakyThrows
    public void saveShortestPathMatrix(List<List<UUID>> matrix, List<UUID> vertexList) {
        long timestamp = System.currentTimeMillis();
        String json = objectMapper.writeValueAsString(matrix);
        redisTemplate.opsForValue().set(SHORTEST_PATH_MATRIX_KEY, json);
        redisTemplate.delete(VERTEX_LIST_KEY);
        redisTemplate.opsForList().rightPushAll(VERTEX_LIST_KEY, vertexList.stream().map(UUID::toString).toList());
        redisTemplate.opsForValue().set(CACHE_UPDATE_TIMESTAMP_KEY, String.valueOf(timestamp));
    }

    public Optional<List<List<UUID>>> retrieveShortestPathMatrix() {
        if (!isCacheUpToDate()) {
            return Optional.empty();
        }

        return Optional.ofNullable(redisTemplate.opsForValue().get(SHORTEST_PATH_MATRIX_KEY))
                .map(json -> {
                    try {
                        var typeRef = new TypeReference<List<List<UUID>>>() {
                        };
                        return objectMapper.readValue(json, typeRef);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public List<UUID> retrieveVertexList() {
        return redisTemplate.opsForList().range(VERTEX_LIST_KEY, 0, -1)
                .stream()
                .map(UUID::fromString)
                .toList();
    }

    public boolean isCacheUpToDate() {
        String cacheUpdateTimestamp = redisTemplate.opsForValue().get(CACHE_UPDATE_TIMESTAMP_KEY);
        String lastGraphInteractionTimestamp = redisTemplate.opsForValue().get(LAST_GRAPH_INTERACTION_TIMESTAMP_KEY);

        if (cacheUpdateTimestamp == null) {
            return false;
        }

        if (lastGraphInteractionTimestamp == null) {
            return true;
        }

        return Long.parseLong(cacheUpdateTimestamp) > Long.parseLong(lastGraphInteractionTimestamp);
    }
}

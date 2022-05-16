package com.ceratipa.graphpathfinder.web;

import com.ceratipa.graphpathfinder.core.domain.service.PathFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PathController {
    private final PathFinder pathFinder;

    @GetMapping("shortest-path")
    public List<UUID> findShortestPath(@RequestParam("source") UUID sourceVertexId,
                                       @RequestParam("destination") UUID destinationVertexId) {
        return pathFinder.findShortestPath(sourceVertexId, destinationVertexId);
    }
}

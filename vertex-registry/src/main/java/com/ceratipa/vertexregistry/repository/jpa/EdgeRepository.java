package com.ceratipa.vertexregistry.repository.jpa;

import com.ceratipa.vertexregistry.core.domain.entity.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EdgeRepository extends JpaRepository<Edge, UUID> {
    @Query("from Edge e where e.from.id = :vertex1 and e.to.id = :vertex2 or e.to.id = :vertex1 and e.from.id = :vertex2")
    List<Edge> findByVertexIds(UUID vertex1, UUID vertex2);

    List<Edge> findByFromIdOrToId(UUID fromId, UUID toId);
}

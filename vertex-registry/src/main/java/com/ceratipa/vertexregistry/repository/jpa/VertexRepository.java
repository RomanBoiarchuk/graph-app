package com.ceratipa.vertexregistry.repository.jpa;

import com.ceratipa.vertexregistry.core.domain.entity.Vertex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VertexRepository extends JpaRepository<Vertex, UUID> {
}

package com.ceratipa.graphpathfinder.repository.jpa;

import com.ceratipa.graphpathfinder.core.domain.entity.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EdgeRepository extends JpaRepository<Edge, UUID> {
}

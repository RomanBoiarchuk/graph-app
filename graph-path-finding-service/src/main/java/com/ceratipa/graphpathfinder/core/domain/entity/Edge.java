package com.ceratipa.graphpathfinder.core.domain.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
public class Edge {
    @Id
    private UUID id;

    private BigDecimal weight;

    private UUID fromVertexId;

    private UUID toVertexId;
}

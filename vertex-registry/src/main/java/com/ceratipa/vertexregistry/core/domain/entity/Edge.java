package com.ceratipa.vertexregistry.core.domain.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
public class Edge {
    @Id
    private UUID id;

    @DecimalMin(value = "0")
    @Digits(integer = 19, fraction = 2)
    @NotNull(message = "Weight can't be null.")
    private BigDecimal weight;

    @ManyToOne
    @JoinColumn(name = "from_vertex")
    @NotNull(message = "Vertex id can't be null.")
    private Vertex from;

    @ManyToOne
    @JoinColumn(name = "to_vertex")
    @NotNull(message = "Vertex id can't be null.")
    private Vertex to;
}

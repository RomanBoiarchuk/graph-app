package com.ceratipa.vertexregistry.core.domain.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Entity
public class Vertex {
    @Id
    private UUID id;

    @NotNull(message = "Name can't be null.")
    private String name;
}

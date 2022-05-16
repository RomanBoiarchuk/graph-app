package com.ceratipa.graphpathfinder.core.domain.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class Vertex {
    @Id
    private UUID id;
}

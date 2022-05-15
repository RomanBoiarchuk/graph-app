package com.ceratipa.vertexregistry.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class JacksonConfig {
    private final ObjectMapper objectMapper;

    @PostConstruct
    private void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }
}

package de.home.micronaut.rest.notes.common;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.inject.Singleton;

@Singleton
public class JsonMapper {

    private final ObjectMapper mapper;

    public JsonMapper() {
        this.mapper = new ObjectMapper();
        this.mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        this.mapper.registerModule(new JavaTimeModule());
    }

    public ObjectMapper getObjectMapper() {
        return this.mapper;
    }
}

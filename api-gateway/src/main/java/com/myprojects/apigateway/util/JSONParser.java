package com.myprojects.apigateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JSONParser {

    private final ObjectMapper mapper;

    public JsonNode parse(String stringJSON) {
        try {
            return mapper.readTree(stringJSON);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

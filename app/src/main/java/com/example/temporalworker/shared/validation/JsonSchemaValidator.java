package com.example.temporalworker.shared.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Set;

@Component
public class JsonSchemaValidator {
    private final ObjectMapper objectMapper;
    private final JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);

    public JsonSchemaValidator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Set<ValidationMessage> validate(String schemaClasspathLocation, JsonNode instance) throws Exception {
        try (InputStream is = new ClassPathResource(schemaClasspathLocation).getInputStream()) {
            JsonSchema schema = schemaFactory.getSchema(is);
            return schema.validate(instance);
        }
    }
}



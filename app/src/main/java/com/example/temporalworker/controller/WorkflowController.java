package com.example.temporalworker.controller;

import com.example.temporalworker.shared.constants.TaskQueues;
import com.example.temporalworker.shared.options.WorkflowOptionsFactory;
import com.example.temporalworker.shared.validation.JsonSchemaValidator;
import com.example.temporalworker.workflows.MyWorkflow;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.networknt.schema.ValidationMessage;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final WorkflowClient workflowClient;
    private final ObjectMapper objectMapper;
    private final JsonSchemaValidator schemaValidator;

    public WorkflowController(WorkflowClient workflowClient, ObjectMapper objectMapper, JsonSchemaValidator schemaValidator) {
        this.workflowClient = workflowClient;
        this.objectMapper = objectMapper;
        this.schemaValidator = schemaValidator;
    }

    public static class StartRequest {
        public String input;
        public String getInput() { return input; }
        public void setInput(String input) { this.input = input; }
    }

    @PostMapping("/my")
    public ResponseEntity<String> startMyWorkflow(@RequestBody StartRequest request) {
        // JSON Schema validation
        try {
            ObjectNode node = objectMapper.valueToTree(request);
            var errors = schemaValidator.validate("schemas/start-request.schema.json", node);
            if (!errors.isEmpty()) {
                String message = errors.stream().map(ValidationMessage::getMessage).sorted().reduce((a,b) -> a + "; " + b).orElse("Invalid request");
                return ResponseEntity.badRequest().body(message);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Schema validation failed: " + e.getMessage());
        }
        WorkflowOptions options = WorkflowOptionsFactory.withTaskQueue(TaskQueues.MY);
        MyWorkflow stub = workflowClient.newWorkflowStub(MyWorkflow.class, options);
        return ResponseEntity.ok(stub.execute(request.input));
    }
}



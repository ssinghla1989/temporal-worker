package com.example.temporalworker.controller;

import com.example.temporalworker.shared.options.WorkflowOptionsFactory;
import com.example.temporalworker.shared.validation.JsonSchemaValidator;
import com.example.temporalworker.workflows.MyWorkflow;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.networknt.schema.ValidationMessage;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.WorkflowIdReusePolicy;
import io.temporal.client.WorkflowExecutionAlreadyStarted;
import org.springframework.beans.factory.annotation.Value;
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
    private final String taskQueue;

    public WorkflowController(WorkflowClient workflowClient, ObjectMapper objectMapper, JsonSchemaValidator schemaValidator,
                              @Value("${temporal.taskQueue:MY_TASK_QUEUE}") String taskQueue) {
        this.workflowClient = workflowClient;
        this.objectMapper = objectMapper;
        this.schemaValidator = schemaValidator;
        this.taskQueue = taskQueue;
    }

    public static class StartRequest {
        public String input;
        public String workflowId; // optional deterministic ID provided by client
        public String getInput() { return input; }
        public void setInput(String input) { this.input = input; }
        public String getWorkflowId() { return workflowId; }
        public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
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
        // Compute deterministic workflowId if provided, otherwise derive from input
        String workflowId = (request.workflowId != null && !request.workflowId.isBlank())
                ? request.workflowId
                : ("my-" + Integer.toHexString(request.input.hashCode()));

        WorkflowOptions options = WorkflowOptionsFactory.withTaskQueueIdAndPolicy(
                taskQueue,
                workflowId,
                WorkflowIdReusePolicy.REJECT_DUPLICATE);

        MyWorkflow stub = workflowClient.newWorkflowStub(MyWorkflow.class, options);
        try {
            WorkflowExecution execution = WorkflowClient.start(stub::execute, request.input);
            ObjectNode response = objectMapper.createObjectNode();
            response.put("workflowId", execution.getWorkflowId());
            response.put("runId", execution.getRunId());
            return ResponseEntity.accepted().body(response.toString());
        } catch (WorkflowExecutionAlreadyStarted e) {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("workflowId", workflowId);
            return ResponseEntity.accepted().body(response.toString());
        }
    }
}



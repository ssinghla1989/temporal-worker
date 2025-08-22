package com.example.temporalworker.controller;

import com.example.temporalworker.shared.constants.TaskQueues;
import com.example.temporalworker.shared.options.WorkflowOptionsFactory;
import com.example.temporalworker.workflows.MyWorkflow;
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

    public WorkflowController(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    public static class StartRequest {
        public String input;
        public String getInput() { return input; }
        public void setInput(String input) { this.input = input; }
    }

    @PostMapping("/my")
    public ResponseEntity<String> startMyWorkflow(@RequestBody StartRequest request) {
        WorkflowOptions options = WorkflowOptionsFactory.withTaskQueue(TaskQueues.MY);
        MyWorkflow stub = workflowClient.newWorkflowStub(MyWorkflow.class, options);
        return ResponseEntity.ok(stub.execute(request.input));
    }
}



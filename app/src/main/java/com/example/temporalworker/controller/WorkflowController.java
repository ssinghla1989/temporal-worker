package com.example.temporalworker.controller;

import com.example.temporalworker.workflows.MyWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final WorkflowClient workflowClient;
    private final String taskQueue;

    public WorkflowController(WorkflowClient workflowClient,
                              @Value("${temporal.taskQueue:MY_TASK_QUEUE}") String taskQueue) {
        this.workflowClient = workflowClient;
        this.taskQueue = taskQueue;
    }

    public static class StartRequest {
        public String input;
        public String getInput() { return input; }
        public void setInput(String input) { this.input = input; }
    }

    @PostMapping("/my")
    public ResponseEntity<String> startMyWorkflow(@RequestBody StartRequest request) {
        MyWorkflow stub = workflowClient.newWorkflowStub(
                MyWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue(taskQueue)
                        .setWorkflowId("my-" + UUID.randomUUID())
                        .build());

        String result = stub.execute(request.input);
        return ResponseEntity.ok(result);
    }
}



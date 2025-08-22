package com.example.temporalworker.controller;

import com.example.temporalworker.service.MyWorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final MyWorkflowService myWorkflowService;

    public WorkflowController(MyWorkflowService myWorkflowService) {
        this.myWorkflowService = myWorkflowService;
    }

    public static class StartRequest {
        public String input;
        public String getInput() { return input; }
        public void setInput(String input) { this.input = input; }
    }

    @PostMapping("/my")
    public ResponseEntity<String> startMyWorkflow(@RequestBody StartRequest request) {
        String result = myWorkflowService.startMyWorkflow(request.input);
        return ResponseEntity.ok(result);
    }
}



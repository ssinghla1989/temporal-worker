package com.example.temporalworker.service;

import com.example.temporalworker.shared.constants.TaskQueues;
import com.example.temporalworker.shared.options.WorkflowOptionsFactory;
import com.example.temporalworker.workflows.MyWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.stereotype.Service;

@Service
public class MyWorkflowService {
    private final WorkflowClient workflowClient;

    public MyWorkflowService(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    public String startMyWorkflow(String input) {
        WorkflowOptions options = WorkflowOptionsFactory.withTaskQueue(TaskQueues.MY);
        MyWorkflow stub = workflowClient.newWorkflowStub(MyWorkflow.class, options);
        return stub.execute(input);
    }
}



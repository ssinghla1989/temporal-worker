package com.example.temporalworker.shared.options;

import io.temporal.client.WorkflowOptions;
import io.temporal.common.WorkflowIdReusePolicy;
import java.time.Duration;

import java.util.UUID;

public final class WorkflowOptionsFactory {
    private WorkflowOptionsFactory() {}

    public static WorkflowOptions withTaskQueue(String taskQueue) {
        return WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(UUID.randomUUID().toString())
                .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.REJECT_DUPLICATE)
                .setWorkflowRunTimeout(Duration.ofMinutes(5))
                .setWorkflowTaskTimeout(Duration.ofSeconds(10))
                .build();
    }

    public static WorkflowOptions withTaskQueueAndId(String taskQueue, String workflowId) {
        return WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(workflowId)
                .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.REJECT_DUPLICATE)
                .setWorkflowRunTimeout(Duration.ofMinutes(5))
                .setWorkflowTaskTimeout(Duration.ofSeconds(10))
                .build();
    }

    public static WorkflowOptions withTaskQueueIdAndPolicy(String taskQueue, String workflowId, WorkflowIdReusePolicy policy) {
        return WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(workflowId)
                .setWorkflowIdReusePolicy(policy)
                .setWorkflowRunTimeout(Duration.ofMinutes(5))
                .setWorkflowTaskTimeout(Duration.ofSeconds(10))
                .build();
    }
}



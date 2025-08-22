package com.example.temporalworker.shared.options;

import io.temporal.client.WorkflowOptions;

import java.util.UUID;

public final class WorkflowOptionsFactory {
    private WorkflowOptionsFactory() {}

    public static WorkflowOptions withTaskQueue(String taskQueue) {
        return WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(UUID.randomUUID().toString())
                .build();
    }

    public static WorkflowOptions withTaskQueueAndId(String taskQueue, String workflowId) {
        return WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(workflowId)
                .build();
    }
}



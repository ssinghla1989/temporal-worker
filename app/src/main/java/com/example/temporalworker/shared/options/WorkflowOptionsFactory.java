package com.example.temporalworker.shared.options;

import io.temporal.client.WorkflowOptions;
import io.temporal.api.enums.v1.WorkflowIdConflictPolicy;
import java.time.Duration;

import java.util.UUID;

public final class WorkflowOptionsFactory {
    private WorkflowOptionsFactory() {}

    public static WorkflowOptions withTaskQueue(String taskQueue) {
        return WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(UUID.randomUUID().toString())
                .setWorkflowIdConflictPolicy(WorkflowIdConflictPolicy.WORKFLOW_ID_CONFLICT_POLICY_REJECT_WORKFLOW)
                .setWorkflowRunTimeout(Duration.ofMinutes(5))
                .setWorkflowTaskTimeout(Duration.ofSeconds(10))
                .build();
    }

    public static WorkflowOptions withTaskQueueAndId(String taskQueue, String workflowId) {
        return WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(workflowId)
                .setWorkflowIdConflictPolicy(WorkflowIdConflictPolicy.WORKFLOW_ID_CONFLICT_POLICY_REJECT_WORKFLOW)
                .setWorkflowRunTimeout(Duration.ofMinutes(5))
                .setWorkflowTaskTimeout(Duration.ofSeconds(10))
                .build();
    }

    public static WorkflowOptions withTaskQueueIdAndConflictPolicy(String taskQueue, String workflowId, WorkflowIdConflictPolicy policy) {
        return WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(workflowId)
                .setWorkflowIdConflictPolicy(policy)
                .setWorkflowRunTimeout(Duration.ofMinutes(5))
                .setWorkflowTaskTimeout(Duration.ofSeconds(10))
                .build();
    }
}



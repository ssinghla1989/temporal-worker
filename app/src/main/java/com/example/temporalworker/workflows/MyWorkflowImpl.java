package com.example.temporalworker.workflows;

import com.example.temporalworker.activities.MyActivity;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class MyWorkflowImpl implements MyWorkflow {
    private final MyActivity activities = Workflow.newActivityStub(
            MyActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .build());

    @Override
    public String execute(String input) {
        return activities.process(input);
    }
}



package com.example.temporalworker.workflows;

import com.example.temporalworker.activities.ExternalApiActivity;
import com.example.temporalworker.shared.options.ActivityOptionsFactory;
import io.temporal.workflow.Workflow;

public class MyWorkflowImpl implements MyWorkflow {
    private final ExternalApiActivity externalApiActivity = Workflow.newActivityStub(
            ExternalApiActivity.class,
            ActivityOptionsFactory.defaultOptions());

    @Override
    public String execute(String input) {
        return externalApiActivity.process(input);
    }
}



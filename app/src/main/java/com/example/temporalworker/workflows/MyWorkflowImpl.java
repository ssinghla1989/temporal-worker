package com.example.temporalworker.workflows;

import com.example.temporalworker.activities.MyActivity;
import com.example.temporalworker.shared.options.ActivityOptionsFactory;
import io.temporal.workflow.Workflow;

public class MyWorkflowImpl implements MyWorkflow {
    private final MyActivity activities = Workflow.newActivityStub(
            MyActivity.class,
            ActivityOptionsFactory.defaultOptions());

    @Override
    public String execute(String input) {
        return activities.process(input);
    }
}



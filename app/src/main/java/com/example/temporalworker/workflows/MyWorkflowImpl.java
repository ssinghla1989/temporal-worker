package com.example.temporalworker.workflows;

import com.example.temporalworker.activities.EchoActivity;
import com.example.temporalworker.shared.options.ActivityOptionsFactory;
import io.temporal.workflow.Workflow;

public class MyWorkflowImpl implements MyWorkflow {
    private final EchoActivity echoActivity = Workflow.newActivityStub(
            EchoActivity.class,
            ActivityOptionsFactory.defaultOptions());

    @Override
    public String execute(String input) {
        return echoActivity.process(input);
    }
}



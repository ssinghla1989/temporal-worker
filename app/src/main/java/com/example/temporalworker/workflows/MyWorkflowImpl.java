package com.example.temporalworker.workflows;

import com.example.temporalworker.activities.EchoActivity;
import com.example.temporalworker.shared.options.ActivityOptionsFactory;
import io.temporal.workflow.Workflow;

public class MyWorkflowImpl implements MyWorkflow {
    private final EchoActivity echoProcess = Workflow.newActivityStub(
            EchoActivity.class,
            ActivityOptionsFactory.optionsForProcess());
    private final EchoActivity echoPostProcess = Workflow.newActivityStub(
            EchoActivity.class,
            ActivityOptionsFactory.optionsForPostProcess());

    @Override
    public String execute(String input) {
        String processed = echoProcess.process(input);
        // Example of using a different method with different options if needed in future:
        // String posted = echoPostProcess.postProcess(input, 1);
        return processed;
    }
}



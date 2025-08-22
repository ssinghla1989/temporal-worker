package com.example.temporalworker.worker;

import com.example.temporalworker.activities.EchoActivity;
import com.example.temporalworker.workflows.MyWorkflowImpl;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyWorkerRegistration implements WorkerRegistration {

    private final String taskQueue;
    private final EchoActivity echoActivity;

    public MyWorkerRegistration(@Value("${temporal.taskQueue:MY_TASK_QUEUE}") String taskQueue,
                                EchoActivity echoActivity) {
        this.taskQueue = taskQueue;
        this.echoActivity = echoActivity;
    }

    @Override
    public void register(WorkerFactory factory) {
        Worker worker = factory.newWorker(taskQueue);
        worker.registerWorkflowImplementationTypes(MyWorkflowImpl.class);
        worker.registerActivitiesImplementations(echoActivity);
    }
}



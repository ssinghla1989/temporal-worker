package com.example.temporalworker.worker;

import com.example.temporalworker.activities.MyActivity;
import com.example.temporalworker.workflows.MyWorkflowImpl;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyWorkerRegistration implements WorkerRegistration {

    private final String taskQueue;
    private final MyActivity myActivity;

    public MyWorkerRegistration(@Value("${temporal.taskQueue:MY_TASK_QUEUE}") String taskQueue,
                                MyActivity myActivity) {
        this.taskQueue = taskQueue;
        this.myActivity = myActivity;
    }

    @Override
    public void register(WorkerFactory factory) {
        Worker worker = factory.newWorker(taskQueue);
        worker.registerWorkflowImplementationTypes(MyWorkflowImpl.class);
        worker.registerActivitiesImplementations(myActivity);
    }
}



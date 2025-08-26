package com.example.temporalworker.worker;

import com.example.temporalworker.activities.HttpActivity;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyWorkerRegistration implements WorkerRegistration {

    private final String taskQueue;
    private final HttpActivity httpActivity;

    public MyWorkerRegistration(@Value("${temporal.taskQueue:MY_TASK_QUEUE}") String taskQueue,
                                HttpActivity httpActivity) {
        this.taskQueue = taskQueue;
        this.httpActivity = httpActivity;
    }

    @Override
    public void register(WorkerFactory factory) {
        Worker worker = factory.newWorker(taskQueue);
        worker.registerActivitiesImplementations(httpActivity);
    }
}



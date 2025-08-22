package com.example.temporalworker.worker;

import io.temporal.worker.WorkerFactory;

public interface WorkerRegistration {
    void register(WorkerFactory factory);
}



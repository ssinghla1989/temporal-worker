package com.example.temporalworker.config;

import com.example.temporalworker.activities.MyActivity;
import com.example.temporalworker.activities.MyActivityImpl;
import com.example.temporalworker.worker.WorkerRegistration;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.List;

@Configuration
public class TemporalConfig {

    @Value("${temporal.namespace:default}")
    private String namespace;

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newLocalServiceStubs();
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs service) {
        return WorkflowClient.newInstance(service, WorkflowClientOptions.newBuilder().setNamespace(namespace).build());
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient client) {
        return WorkerFactory.newInstance(client);
    }

    @Bean
    public MyActivity myActivity(MyActivityImpl impl) {
        return impl;
    }

    @Bean
    @ConditionalOnProperty(name = "temporal.worker.enabled", havingValue = "true", matchIfMissing = true)
    public CommandLineRunner temporalRunner(WorkerFactory factory, List<WorkerRegistration> registrations) {
        return args -> {
            for (WorkerRegistration registration : registrations) {
                registration.register(factory);
            }
            factory.start();
        };
    }
}



package com.example.temporalworker.shared.options;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;

import java.time.Duration;

public final class ActivityOptionsFactory {
    private ActivityOptionsFactory() {}

    public static ActivityOptions defaultOptions() {
        return ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(10))
                .setScheduleToCloseTimeout(Duration.ofMinutes(1))
                .setRetryOptions(RetryOptions.newBuilder()
                        .setInitialInterval(Duration.ofSeconds(1))
                        .setMaximumInterval(Duration.ofSeconds(10))
                        .setBackoffCoefficient(2.0)
                        .setMaximumAttempts(3)
                        .build())
                .build();
    }

    public static ActivityOptions optionsForProcess() {
        return ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(10))
                .setScheduleToCloseTimeout(Duration.ofMinutes(1))
                .setRetryOptions(RetryOptions.newBuilder()
                        .setInitialInterval(Duration.ofSeconds(1))
                        .setMaximumInterval(Duration.ofSeconds(10))
                        .setBackoffCoefficient(2.0)
                        .setMaximumAttempts(4)
                        .setDoNotRetry("HttpClientError")
                        .build())
                .build();
    }

    public static ActivityOptions optionsForPostProcess() {
        return ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(15))
                .setScheduleToCloseTimeout(Duration.ofMinutes(2))
                .setRetryOptions(RetryOptions.newBuilder()
                        .setInitialInterval(Duration.ofSeconds(2))
                        .setMaximumInterval(Duration.ofSeconds(15))
                        .setBackoffCoefficient(2.0)
                        .setMaximumAttempts(5)
                        .setDoNotRetry("HttpClientError")
                        .build())
                .build();
    }
}



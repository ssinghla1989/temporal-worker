package com.example.temporalworker.shared.options;

import io.temporal.activity.ActivityOptions;

import java.time.Duration;

public final class ActivityOptionsFactory {
    private ActivityOptionsFactory() {}

    public static ActivityOptions defaultOptions() {
        return ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(10))
                .build();
    }
}



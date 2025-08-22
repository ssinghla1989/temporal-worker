package com.example.temporalworker.activities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ExternalApiActivity {
    @ActivityMethod
    String process(String input);
}




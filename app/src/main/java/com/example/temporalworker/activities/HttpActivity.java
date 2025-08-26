package com.example.temporalworker.activities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import java.util.Map;

@ActivityInterface
public interface HttpActivity {
    /**
     * Perform an HTTP call defined by the spec. Returns a map with keys: status, headers, body.
     */
    @ActivityMethod
    Map<String, Object> call(HttpCallSpec spec);
}



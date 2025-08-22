package com.example.temporalworker.activities;

import com.example.temporalworker.clients.ExternalApiService;
import org.springframework.stereotype.Component;
import retrofit2.Response;

@Component
public class MyActivityImpl implements MyActivity {
    private final ExternalApiService externalApiService;

    public MyActivityImpl(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @Override
    public String process(String input) {
        try {
            Response<String> response = externalApiService.echo(input).execute();
            if (response.isSuccessful() && response.body() != null) {
                return "Processed via API: " + response.body();
            }
            return "Processed with non-200: " + response.code();
        } catch (Exception e) {
            return "Processed with error: " + e.getMessage();
        }
    }
}



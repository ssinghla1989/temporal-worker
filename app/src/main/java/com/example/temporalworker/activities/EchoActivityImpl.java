package com.example.temporalworker.activities;

import com.example.temporalworker.clients.ExternalApiService;
import com.example.temporalworker.clients.model.HttpBinAnythingResponse;
import org.springframework.stereotype.Component;
import retrofit2.Response;

@Component
public class EchoActivityImpl implements EchoActivity {
    private final ExternalApiService externalApiService;

    public EchoActivityImpl(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @Override
    public String process(String input) {
        try {
            Response<HttpBinAnythingResponse> response = externalApiService.echo(input).execute();
            if (response.isSuccessful() && response.body() != null) {
                HttpBinAnythingResponse body = response.body();
                return "Processed via API: method=" + body.getMethod() + ", url=" + body.getUrl();
            }
            return "Processed with non-200: " + response.code();
        } catch (Exception e) {
            return "Processed with error: " + e.getMessage();
        }
    }
}



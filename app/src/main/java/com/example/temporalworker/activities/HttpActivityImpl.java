package com.example.temporalworker.activities;

import io.temporal.failure.ApplicationFailure;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Minimal HTTP Activity for basic requests.
 */
@Component
public class HttpActivityImpl implements HttpActivity {
    private final OkHttpClient httpClient;

    public HttpActivityImpl(OkHttpClient okHttpClient) {
        this.httpClient = okHttpClient;
    }

    @Override
    public Map<String, Object> call(HttpCallSpec spec) {
        try {
            Request.Builder rb = new Request.Builder().url(spec.getUrl());

            if (spec.getHeaders() != null) {
                for (Map.Entry<String, String> e : spec.getHeaders().entrySet()) {
                    rb.addHeader(e.getKey(), e.getValue());
                }
            }

            String method = spec.getMethod() == null ? "GET" : spec.getMethod().toUpperCase();
            RequestBody body = null;
            if (spec.getBody() != null && !spec.getBody().isBlank()) {
                String ct = spec.getContentType() != null ? spec.getContentType() : "application/json";
                body = RequestBody.create(spec.getBody(), MediaType.parse(ct));
            }

            switch (method) {
                case "GET" -> rb.get();
                case "DELETE" -> rb.delete(body != null ? body : RequestBody.create(new byte[0]));
                case "POST" -> rb.post(body != null ? body : RequestBody.create(new byte[0]));
                case "PUT" -> rb.put(body != null ? body : RequestBody.create(new byte[0]));
                case "PATCH" -> rb.patch(body != null ? body : RequestBody.create(new byte[0]));
                default -> throw ApplicationFailure.newNonRetryableFailure("Unsupported method: " + method, "InvalidHttpMethod");
            }

            try (Response response = httpClient.newCall(rb.build()).execute()) {
                int code = response.code();
                String responseBody = response.body() != null ? response.body().string() : null;

                if (code >= 200 && code < 300) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", code);
                    result.put("body", responseBody);
                    return result;
                }
                String msg = "HTTP non-2xx: " + code;
                if (code >= 500) {
                    throw ApplicationFailure.newFailure(msg, "HttpServerError");
                }
                throw ApplicationFailure.newNonRetryableFailure(msg, "HttpClientError");
            }
        } catch (ApplicationFailure af) {
            throw af;
        } catch (Exception e) {
            throw ApplicationFailure.newFailure("HTTP call failed: " + e.getMessage(), "NetworkError");
        }
    }
}



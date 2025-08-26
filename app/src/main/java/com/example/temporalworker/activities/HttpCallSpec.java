package com.example.temporalworker.activities;

import java.util.Map;

/**
 * Minimal HTTP call specification for the HttpActivity.
 */
public class HttpCallSpec {
    private String method; // GET, POST, PUT, PATCH, DELETE
    private String url; // absolute URL
    private Map<String, String> headers; // optional
    private String body; // optional raw body
    private String contentType; // optional; defaults to application/json when body provided

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
}



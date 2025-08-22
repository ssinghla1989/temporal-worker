package com.example.temporalworker.clients.model;

import java.util.Map;

public class HttpBinAnythingResponse {
    private String url;
    private String method;
    private Map<String, String> args;

    public HttpBinAnythingResponse() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}



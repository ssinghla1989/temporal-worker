package com.example.temporalworker.clients.model;

import java.util.Map;

public class HttpBinPostResponse {
    private String url;
    private Map<String, Object> json;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getJson() {
        return json;
    }

    public void setJson(Map<String, Object> json) {
        this.json = json;
    }
}



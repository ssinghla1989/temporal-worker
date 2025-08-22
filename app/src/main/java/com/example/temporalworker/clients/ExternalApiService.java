package com.example.temporalworker.clients;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ExternalApiService {
    @GET("/anything/{name}")
    Call<String> echo(@Path("name") String name);
}



package com.example.temporalworker.clients;

import com.example.temporalworker.clients.model.HttpBinAnythingResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ExternalApiService {
    @GET("/anything/{name}")
    Call<HttpBinAnythingResponse> echo(@Path("name") String name);
}



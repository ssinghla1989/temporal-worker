package com.example.temporalworker.activities;

import com.example.temporalworker.clients.model.HttpBinAnythingResponse;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Component
public class EchoActivityImpl implements EchoActivity {
    private final EchoApi echoApi;

    interface EchoApi {
        @GET("/anything/{name}")
        Call<HttpBinAnythingResponse> echo(@Path("name") String name);
    }

    public EchoActivityImpl(Retrofit retrofit) {
        this.echoApi = retrofit.create(EchoApi.class);
    }

    @Override
    public String process(String input) {
        try {
            Response<HttpBinAnythingResponse> response = echoApi.echo(input).execute();
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



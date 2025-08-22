package com.example.temporalworker.activities;

import com.example.temporalworker.clients.model.HttpBinAnythingResponse;
import com.example.temporalworker.clients.model.HttpBinPostRequest;
import com.example.temporalworker.clients.model.HttpBinPostResponse;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Component
public class EchoActivityImpl implements EchoActivity {
    private final Retrofit retrofit;
    private final EchoApi echoApi;

    interface EchoApi {
        @GET("/anything/{name}")
        Call<HttpBinAnythingResponse> echo(@Path("name") String name);
    }

    public EchoActivityImpl(Retrofit retrofit) {
        this.retrofit = retrofit;
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

    @Override
    public String postProcess(String name, int count) {
        try {
            HttpBinPostRequest request = new HttpBinPostRequest();
            request.setName(name);
            request.setCount(count);

            // define POST on the fly
            Call<HttpBinPostResponse> call = retrofit.create(PostApi.class).create(request);
            Response<HttpBinPostResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                HttpBinPostResponse body = response.body();
                return "Posted to: " + body.getUrl();
            }
            return "POST non-200: " + response.code();
        } catch (Exception e) {
            return "POST error: " + e.getMessage();
        }
    }

    interface PostApi {
        @retrofit2.http.POST("/post")
        Call<HttpBinPostResponse> create(@retrofit2.http.Body HttpBinPostRequest body);
    }
}



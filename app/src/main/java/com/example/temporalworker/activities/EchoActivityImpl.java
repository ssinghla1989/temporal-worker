package com.example.temporalworker.activities;

import com.example.temporalworker.clients.model.HttpBinAnythingResponse;
import com.example.temporalworker.clients.model.HttpBinPostRequest;
import com.example.temporalworker.clients.model.HttpBinPostResponse;
import io.temporal.failure.ApplicationFailure;
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
            int code = response.code();
            String msg = "HTTP non-2xx in process: " + code;
            if (code >= 500) {
                throw ApplicationFailure.newFailure(msg, "HttpServerError");
            }
            throw ApplicationFailure.newNonRetryableFailure(msg, "HttpClientError");
        } catch (Exception e) {
            throw ApplicationFailure.newFailure("Network error in process: " + e.getMessage(), "NetworkError");
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
            int code = response.code();
            String msg = "HTTP non-2xx in postProcess: " + code;
            if (code >= 500) {
                throw ApplicationFailure.newFailure(msg, "HttpServerError");
            }
            throw ApplicationFailure.newNonRetryableFailure(msg, "HttpClientError");
        } catch (Exception e) {
            throw ApplicationFailure.newFailure("Network error in postProcess: " + e.getMessage(), "NetworkError");
        }
    }

    interface PostApi {
        @retrofit2.http.POST("/post")
        Call<HttpBinPostResponse> create(@retrofit2.http.Body HttpBinPostRequest body);
    }
}



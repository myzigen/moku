package com.mhr.mobile.api.client;

import android.app.Activity;
import com.mhr.mobile.api.endpoint.EndpointApi;
import java.util.List;
import java.util.concurrent.Executors;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientApi<T> {

  private EndpointApi endpoint;
  private Activity activity;
  private OkHttpClient client;
  private HttpLoggingInterceptor interceptor;

  public ClientApi(Activity activity) {
    this.activity = activity;
    interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://api.qiospro.my.id/")
            .addConverterFactory(GsonConverterFactory.create())
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .client(client)
            .build();

    endpoint = retrofit.create(EndpointApi.class);
  }

  public interface Callback<T> {
    void onRequest();

    void onDataChange(List<T> data);

    void onFailure(String errorMessage);
  }

  public interface CallbackSingle<T> {
    void onRequest();

    void onDataChange(T data);

    void onFailure(String errorMessage);
  }

  public void execute(Call<List<T>> call, Callback<T> callback) {
    callback.onRequest();

    call.enqueue(
        new retrofit2.Callback<List<T>>() {
          @Override
          public void onResponse(Call<List<T>> call, Response<List<T>> response) {
            if (response.isSuccessful() && response.body() != null) {
              activity.runOnUiThread(() -> callback.onDataChange(response.body()));
            } else {
              activity.runOnUiThread(
                  () -> callback.onFailure("Response gagal: " + response.code()));
            }
          }

          @Override
          public void onFailure(Call<List<T>> call, Throwable t) {
            activity.runOnUiThread(() -> callback.onFailure(t.getLocalizedMessage()));
          }
        });
  }

  public void executeSingle(Call<T> call, CallbackSingle<T> callback) {
    callback.onRequest();

    call.enqueue(
        new retrofit2.Callback<T>() {
          @Override
          public void onResponse(Call<T> call, Response<T> response) {
            if (response.isSuccessful() && response.body() != null) {
              activity.runOnUiThread(() -> callback.onDataChange(response.body()));
            } else {
              activity.runOnUiThread(
                  () -> callback.onFailure("Response gagal: " + response.code()));
            }
          }

          @Override
          public void onFailure(Call<T> call, Throwable t) {
            activity.runOnUiThread(() -> callback.onFailure(t.getLocalizedMessage()));
          }
        });
  }

  public EndpointApi getEndpoint() {
    return endpoint;
  }
}

package com.mhr.mobile.api.client;

import android.app.Activity;
import android.widget.Toast;
import com.mhr.mobile.api.endpoint.EndpointApi;
import com.mhr.mobile.util.NetworkUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientApiOffline<T> {
  private EndpointApi endpoint;
  private Activity activity;
  private OkHttpClient client;
  private HttpLoggingInterceptor interceptor;

  public ClientApiOffline(Activity activity) {
    this.activity = activity;
    interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

    // 1. Setup cache 5MB
    int cacheSize = 5 * 1024 * 1024;
    File cacheDir = new File(activity.getCacheDir(), "http-cache");
    Cache cache = new Cache(cacheDir, cacheSize);

    // 2. Interceptor untuk online (saat ada internet)
    Interceptor networkInterceptor = chain -> {
      Response response = chain.proceed(chain.request());
      CacheControl cacheControl = new CacheControl.Builder()
              .maxAge(5, TimeUnit.MINUTES) // Cache aktif 5 menit
              .build();

      if (response.networkResponse() != null) {
        activity.runOnUiThread(() -> Toast.makeText(activity, "DARI API", Toast.LENGTH_SHORT).show());
      } else if (response.cacheResponse() != null) {
        activity.runOnUiThread(() -> Toast.makeText(activity, "DARI CACHE", Toast.LENGTH_SHORT).show());
      }

      return response.newBuilder()
              .header("Cache-Control", cacheControl.toString())
              .build();
    };

    // 3. Interceptor untuk offline (paksa baca cache)
    Interceptor offlineInterceptor = chain -> {
      Request request = chain.request();
      if (!NetworkUtil.isNetworkAvailable(activity)) {
        CacheControl cacheControl = new CacheControl.Builder()
                .onlyIfCached()
                .maxStale(7, TimeUnit.DAYS) // cache masih bisa dipakai walau 7 hari lalu
                .build();
        request = request.newBuilder()
                .cacheControl(cacheControl)
                .build();
      }
      return chain.proceed(request);
    };

    // 4. Build OkHttpClient
    client = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(offlineInterceptor)       // tambahkan interceptor offline dulu
            .addInterceptor(interceptor)              // logging
			.addNetworkInterceptor(networkInterceptor) // lalu online
            .cache(cache)
            .build();

    // 5. Retrofit
    Retrofit retrofit = new Retrofit.Builder()
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
    call.enqueue(new retrofit2.Callback<List<T>>() {
      @Override
      public void onResponse(Call<List<T>> call, retrofit2.Response<List<T>> response) {
        if (response.isSuccessful() && response.body() != null) {
          activity.runOnUiThread(() -> callback.onDataChange(response.body()));
        } else {
          activity.runOnUiThread(() -> callback.onFailure("Response gagal: " + response.code()));
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
    call.enqueue(new retrofit2.Callback<T>() {
      @Override
      public void onResponse(Call<T> call, retrofit2.Response<T> response) {
        if (response.isSuccessful() && response.body() != null) {
          activity.runOnUiThread(() -> callback.onDataChange(response.body()));
        } else {
          activity.runOnUiThread(() -> callback.onFailure("Response gagal: " + response.code()));
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
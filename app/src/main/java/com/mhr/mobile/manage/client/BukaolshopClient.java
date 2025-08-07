package com.mhr.mobile.manage.client;

import com.mhr.mobile.manage.call.BukaolshopEndpoint;
import com.mhr.mobile.manage.request.MarketplaceRequest;
import com.mhr.mobile.manage.response.MarketplaceResponse;
import com.mhr.mobile.manage.response.SliderHomeResponse;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BukaolshopClient {
  private BukaolshopEndpoint endpoint;
  private static final String BASE_URL = "https://openapi.bukaolshop.net/v1/";

  private BukaolshopClient() {
    OkHttpClient client = new OkHttpClient.Builder().build();
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    endpoint = retrofit.create(BukaolshopEndpoint.class);
  }

  public void execute(
      MarketplaceRequest request, String token, MarketplaceRequest.RequestSlide callback) {
    callback.onStartRequest();
    Call<SliderHomeResponse> call = endpoint.getGambarSlide(token);
    call.enqueue(
        new Callback<SliderHomeResponse>() {
          @Override
          public void onResponse(
              Call<SliderHomeResponse> call, Response<SliderHomeResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
              request.activity.runOnUiThread(() -> callback.onResponse(response.body()));
            }
          }

          @Override
          public void onFailure(Call<SliderHomeResponse> call, Throwable t) {
            callback.onFailure("Error" + t.getLocalizedMessage());
          }
        });
  }

  public void execute(MarketplaceRequest request, String token, MarketplaceRequest.RequestProduk callback) {
    callback.onStartRequest();
    Call<MarketplaceResponse> call = endpoint.getProduk(token, 1);
    call.enqueue(
        new Callback<MarketplaceResponse>() {
          @Override
          public void onResponse(
              Call<MarketplaceResponse> call, Response<MarketplaceResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
              request.activity.runOnUiThread(() -> callback.onResponse(response.body().getData()));
            }
          }

          @Override
          public void onFailure(Call<MarketplaceResponse> call, Throwable t) {
            callback.onFailure("Error" + t.getLocalizedMessage());
          }
        });
  }

  private static BukaolshopClient mInstance;

  public static synchronized BukaolshopClient getInstance() {
    if (mInstance == null) {
      mInstance = new BukaolshopClient();
    }
    return mInstance;
  }
}

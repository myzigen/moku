package com.mhr.mobile.inquiry.client;

import android.util.Log;
import com.google.gson.Gson;
import com.mhr.mobile.inquiry.call.EndpointInquiry;
import com.mhr.mobile.inquiry.request.InquiryRequest;
import com.mhr.mobile.inquiry.response.InquiryResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InquiryClient {
  private EndpointInquiry endpoint;

  private InquiryClient() {
    OkHttpClient client = new OkHttpClient.Builder().build();
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://mobilepulsa.net/") // API base URL
            .addConverterFactory(GsonConverterFactory.create()) // Gson converter
            .client(client)
            .build();
    endpoint = retrofit.create(EndpointInquiry.class);
  }

  public void execute(
      InquiryRequest request,
      String commands,
      String username,
      String code,
      String hp,
      String ref_id,
      String sign,
      String key,
      int amount, // amount untuk nominal
      InquiryRequest.InquiryCallback listener) {

    listener.onStartLoading();

    String jsonBody = new Gson().toJson(new InquiryRequest(commands, username, code, hp, ref_id, sign, amount));
    //Log.d("JSON REQUEST", jsonBody);

    RequestBody rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);

    Call<InquiryResponse> call = endpoint.getInquiry(rb);
    call.enqueue(
        new Callback<InquiryResponse>() {
          @Override
          public void onResponse(Call<InquiryResponse> call, Response<InquiryResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
              request.activity.runOnUiThread(() -> listener.onResponse(response.body()));
            } else {
              listener.onFailure("Error " + response.code());
            }
          }

          @Override
          public void onFailure(Call<InquiryResponse> call, Throwable t) {
            request.activity.runOnUiThread(() -> listener.onFailure(t.getLocalizedMessage()));
          }
        });
  }

  private static InquiryClient mInstance;

  public static synchronized InquiryClient getInstance() {
    if (mInstance == null) {
      mInstance = new InquiryClient();
    }
    return mInstance;
  }
}

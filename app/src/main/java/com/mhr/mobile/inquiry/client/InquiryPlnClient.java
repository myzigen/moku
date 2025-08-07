package com.mhr.mobile.inquiry.client;

import com.google.gson.Gson;
import com.mhr.mobile.inquiry.call.EndpointInquiry;
import com.mhr.mobile.inquiry.response.InquiryPLNResponse;
import retrofit2.Callback;
import retrofit2.Call;
import okhttp3.MediaType;
import com.mhr.mobile.inquiry.request.InquiryPLNRequest;
import okhttp3.RequestBody;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InquiryPlnClient {

  private EndpointInquiry endpoint;

  private InquiryPlnClient() {
    OkHttpClient client = new OkHttpClient.Builder().build();
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://prepaid.iak.id/") // API base URL
            .addConverterFactory(GsonConverterFactory.create()) // Gson converter
            .client(client)
            .build();
    endpoint = retrofit.create(EndpointInquiry.class);
  }

  public void execute(
      InquiryPLNRequest request,
      String username,
      String customer_id,
      String sign,
      String key,
      InquiryPLNRequest.InquiryCallback listener) {

    listener.onStartLoading();

    String jsonBody =
        new Gson()
            .toJson(
                new InquiryPLNRequest(username, customer_id, sign)); // Ubah request menjadi JSON

    RequestBody rb =
        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);

    Call<InquiryPLNResponse> call = endpoint.getInquiryPln(rb);
    call.enqueue(
        new Callback<InquiryPLNResponse>() {
          @Override
          public void onResponse(Call<InquiryPLNResponse> call, Response<InquiryPLNResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
              request.activity.runOnUiThread(() -> listener.onResponse(response.body()));
            } else {
              listener.onFailure("Error " + response.code());
            }
          }

          @Override
          public void onFailure(Call<InquiryPLNResponse> call, Throwable t) {
            request.activity.runOnUiThread(() -> listener.onFailure(t.getLocalizedMessage()));
          }
        });
  }

  private static InquiryPlnClient mInstance;

  public static synchronized InquiryPlnClient getInstance() {
    if (mInstance == null) {
      mInstance = new InquiryPlnClient();
    }
    return mInstance;
  }
}

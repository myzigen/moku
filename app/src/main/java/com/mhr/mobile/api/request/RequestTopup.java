package com.mhr.mobile.api.request;

import android.app.Activity;
import com.google.gson.Gson;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.response.ResponseTopup;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RequestTopup {
  public Activity activity;
  private String nomor;
  private String token;

  public RequestTopup(Activity activity) {
    this.activity = activity;
  }

  public void setHeader(String token, String nomor) {
    this.token = token;
    this.nomor = nomor;
  }

  public void requestTopup(Callback callback) {
    Map<String, Object> map = new HashMap<>();
    map.put("token", token);
    map.put("nomor", nomor);
    String jsonBody = new Gson().toJson(map);
    RequestBody rb =
        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    ClientApi<ResponseTopup> requestDataOtp = new ClientApi<>(activity);
    Call<ResponseTopup> call = requestDataOtp.getEndpoint().getTopup(rb);

    requestDataOtp.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseTopup>() {
          @Override
          public void onRequest() {
            callback.onRequest();
          }

          @Override
          public void onDataChange(ResponseTopup data) {
            callback.onTopup(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public interface Callback {
    void onRequest();

    void onTopup(ResponseTopup topup);

    void onFailure(String error);
  }
}

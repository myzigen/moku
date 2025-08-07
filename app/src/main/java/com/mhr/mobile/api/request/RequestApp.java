package com.mhr.mobile.api.request;

import android.app.Activity;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.listener.MokuCallback;
import com.mhr.mobile.api.response.ResponsePromosi;
import com.mhr.mobile.api.response.ResponseSetting;
import retrofit2.Call;

public class RequestApp {
  private Activity activity;

  public RequestApp(Activity activity) {
    this.activity = activity;
  }

  public void maintanceStatusRequest(MokuCallback callback) {
    ClientApi<ResponseSetting> api = new ClientApi<>(activity);
    Call<ResponseSetting> call = api.getEndpoint().getMaintance();
    execute(call, callback);
  }

  public void popupRequest(MokuCallback callback) {
    ClientApi<ResponsePromosi> api = new ClientApi<>(activity);
    Call<ResponsePromosi> call = api.getEndpoint().showPopup();
    execute(call, callback);
  }

  private <T> void execute(Call<T> call, MokuCallback callback) {
    new ClientApi<T>(activity)
        .executeSingle(
            call,
            new ClientApi.CallbackSingle<T>() {
              @Override
              public void onRequest() {
                callback.onDataLoading();
              }

              @Override
              public void onDataChange(T data) {
                callback.onDataValue(data);
              }

              @Override
              public void onFailure(String errorMessage) {
                callback.onDataError(errorMessage);
              }
            });
  }
}

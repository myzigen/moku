package com.mhr.mobile.api.request;

import android.app.Activity;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.model.Wilayah;
import java.util.List;
import retrofit2.Call;

public class RequestWilayah {
  private final Activity activity;
  private String keywoard;

  public RequestWilayah(Activity activity) {
    this.activity = activity;
  }

  public void setKeywoard(String keywoard) {
    this.keywoard = keywoard;
  }

  // Ambil Provinsi
  public void getWilayah(Callback callback) {
    ClientApi<Wilayah> client = new ClientApi<>(activity);
    Call<Wilayah> call = client.getEndpoint().searchKecamatan(keywoard);
    client.executeSingle(
        call,
        new ClientApi.CallbackSingle<Wilayah>() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(Wilayah data) {
            callback.onWilayah(data.data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onError(errorMessage);
          }
        });
  }

  // Interface Callback
  public interface Callback {
    void onWilayah(List<Wilayah.Item> wilayahList);

    void onError(String error);
  }
}

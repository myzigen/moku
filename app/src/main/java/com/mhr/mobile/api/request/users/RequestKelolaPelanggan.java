package com.mhr.mobile.api.request.users;

import android.app.Activity;
import com.google.gson.Gson;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.response.ResponsePelanggan;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RequestKelolaPelanggan {
  private Activity activity;
  private String token, aksi, id, nama, no_hp, catatan;

  public RequestKelolaPelanggan(Activity activity) {
    this.activity = activity;
  }

  public static RequestKelolaPelanggan with(Activity activity) {
    return new RequestKelolaPelanggan(activity);
  }

  public void token(String token) {
    this.token = token;
  }

  public void aksi(String aksi) {
    this.aksi = aksi;
  }

  public void id(String id) {
    this.id = id;
  }

  public void nama(String nama) {
    this.nama = nama;
  }

  public void noHp(String no_hp) {
    this.no_hp = no_hp;
  }

  public void catatan(String catatan) {
    this.catatan = catatan;
  }

  public void theSamePelanggan(Callback callback) {
    Map<String, Object> map = new HashMap<>();
    map.put("token", token);
    map.put("aksi", aksi);
    map.put("id", id);
    map.put("nama", nama);
    map.put("no_hp", no_hp);
    map.put("catatan", catatan);

    String jsonBody = new Gson().toJson(map);
    RequestBody rb =
        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    ClientApi<ResponsePelanggan> api = new ClientApi<>(activity);
    Call<ResponsePelanggan> call = api.getEndpoint().kelolaPelanggan(rb);

    api.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponsePelanggan>() {
          @Override
          public void onRequest() {
            callback.onRequest();
          }

          @Override
          public void onDataChange(ResponsePelanggan data) {
            callback.onKelola(data.getData());
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onError(errorMessage);
          }
        });
  }

  public interface Callback {
    void onRequest();

    void onKelola(List<ResponsePelanggan.Data> response);

    void onError(String error);
  }
}

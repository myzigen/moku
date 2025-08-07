package com.mhr.mobile.api.request;

import android.app.Activity;
import com.google.gson.Gson;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.response.ResponseUsers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class RequestLogin {
  private Activity activity;
  private String nomor;
  private String verifyOtp;
  private String nama, namaToko, alamatToko;
  private String pin;
  private String token;
  private long kadaluwarsa;
  private RequestBody rb;

  public long getWaktu() {
    return kadaluwarsa;
  }

  public RequestLogin(Activity activity) {
    this.activity = activity;
  }

  public RequestLogin(String token) {
    this.token = token;
  }

  public RequestLogin setNomor(String nomor) {
    this.nomor = nomor;
    return this;
  }

  public RequestLogin setVerifyOtp(String nomor, String verifyOtp) {
    this.nomor = nomor;
    this.verifyOtp = verifyOtp;
    return this;
  }

  public RequestLogin setUpdateNama(String nama) {
    this.nama = nama;
    return this;
  }

  public void setUpdateNamaToko(String namaToko) {
    this.namaToko = namaToko;
  }

  public void setUpdateAlamatToko(String alamatToko) {
    this.alamatToko = alamatToko;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public RequestLogin setToken(String token) {
    this.token = token;
    return this;
  }

  public void requestUpdate(Callback callbackNama) {
    ClientApi<ResponseBody> requestDataOtp = new ClientApi<>(activity);
    Call<ResponseBody> call =
        requestDataOtp.getEndpoint().updateProfile(token, nama, namaToko, alamatToko);

    requestDataOtp.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseBody>() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(ResponseBody data) {
            callbackNama.onUpdateNama();
          }

          @Override
          public void onFailure(String errorMessage) {
            callbackNama.onFailure(errorMessage);
          }
        });
  }

  public void requestProfile(CallbackProfile callbackProfile) {
    String json = new Gson().toJson(new RequestLogin(token));
    rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    ClientApi<ResponseUsers> requestDataOtp = new ClientApi<>(activity);
    Call<ResponseUsers> call = requestDataOtp.getEndpoint().getUsers(rb);

    requestDataOtp.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseUsers>() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(ResponseUsers data) {
            callbackProfile.onProfile(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callbackProfile.onFailure(errorMessage);
          }
        });
  }

  public interface Callback {
    public void onUpdateNama();

    public void onFailure(String error);
  }

  public interface CallbackProfile {
    void onProfile(ResponseUsers profile);

    void onFailure(String error);
  }
}

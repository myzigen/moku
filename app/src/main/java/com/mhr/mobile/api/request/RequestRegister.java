package com.mhr.mobile.api.request;

import android.app.Activity;
import com.google.gson.Gson;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.response.ResponseRegister;
import com.mhr.mobile.util.FormatUtils;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RequestRegister {
  private Activity activity;
  private RequestBody rb;
  private String nomor, nama, nama_toko, referral, pin, player_id;
  private File fotoFile;

  public RequestRegister(Activity activity) {
    this.activity = activity;
  }

  public static RequestRegister with(Activity activity) {
    return new RequestRegister(activity);
  }

  public RequestRegister(
      String nomor, String nama, String nama_toko, String referral, String pin, String player_id) {
    this.nomor = nomor;
    this.nama = nama;
    this.nama_toko = nama_toko;
    this.referral = referral;
    this.pin = pin;
    this.player_id = player_id;
  }

  public RequestRegister(String referral) {
    this.referral = referral;
  }

  public void setNomor(String nomor) {
    this.nomor = FormatUtils.normalizeNomor(nomor);
  }

  public void setNama(String nama) {
    this.nama = nama;
  }

  public void setNamaToko(String nama_toko) {
    this.nama_toko = nama_toko;
  }

  public void setReferral(String referral) {
    this.referral = referral;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public void setFoto(File file) {
    this.fotoFile = file;
  }

  public void setPlayerId(String player_id) {
    this.player_id = player_id;
  }

  public void cekReferral(CallbackRegister callback) {
    String jsonBody = new Gson().toJson(new ReferralRequest(referral));
    rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    ClientApi<ResponseRegister> regist = new ClientApi<>(activity);
    Call<ResponseRegister> call = regist.getEndpoint().checkReferral(rb);

    regist.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseRegister>() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(ResponseRegister register) {
            callback.onRegister(register);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public void PostRegister(CallbackRegister callback) {
    ClientApi<ResponseRegister> regist = new ClientApi<>(activity);

    RequestBody nomorBody = RequestBody.create(MediaType.parse("text/plain"), nomor);
    RequestBody namaBody = RequestBody.create(MediaType.parse("text/plain"), nama);
    RequestBody namaTokoBody =
        RequestBody.create(MediaType.parse("text/plain"), nama_toko != null ? nama_toko : "");
    RequestBody referralBody =
        RequestBody.create(MediaType.parse("text/plain"), referral != null ? referral : "");
    RequestBody pinBody = RequestBody.create(MediaType.parse("text/plain"), pin);
    RequestBody playerId = RequestBody.create(MediaType.parse("text/plain"), player_id);
    MultipartBody.Part fotoPart = null;
    if (fotoFile != null && fotoFile.exists()) {
      RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), fotoFile);
      fotoPart = MultipartBody.Part.createFormData("foto", fotoFile.getName(), requestFile);
    }

    Call<ResponseRegister> call =
        regist
            .getEndpoint()
            .endpointUserRegister(
                nomorBody, namaBody, namaTokoBody, referralBody, pinBody, playerId, fotoPart);

    regist.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseRegister>() {
          @Override
          public void onRequest() {
            callback.onRequest();
          }

          @Override
          public void onDataChange(ResponseRegister register) {
            callback.onRegister(register);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public interface CallbackRegister {
    void onRequest();

    void onRegister(ResponseRegister register);

    void onFailure(String error);
  }

  private static class ReferralRequest {
    String referral;

    ReferralRequest(String referral) {
      this.referral = referral;
    }
  }
}

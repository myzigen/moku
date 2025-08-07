package com.mhr.mobile.api.request;

import android.app.Activity;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.response.ResponsePelanggan;
import com.mhr.mobile.api.response.ResponseProfile;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.ui.intro.UserSession;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosPreferences;
import java.util.List;
import retrofit2.Call;

public class RequestProfile {
  private Activity activity;
  private String token;
  // Request Tambah Pelanggan
  private String nama;
  private String no_hp;
  private String token_listrik;
  private String tagihan_wifi;
  private String catatan;
  private String nomor;

  public RequestProfile(Activity activity) {
    this.activity = activity;
  }
  // Request Tambah Pelanggan
  public RequestProfile(
      String nama, String no_hp, String tagihan_listrik, String tagihan_wifi, String catatan) {
    this.nama = nama;
    this.no_hp = no_hp;
    this.token_listrik = token_listrik;
    this.tagihan_wifi = tagihan_wifi;
    this.catatan = catatan;
  }

  public void setTambahPelanggan(
      String token,
      String nama,
      String no_hp,
      String tagihan_listrik,
      String tagihan_wifi,
      String catatan) {
    this.token = token;
    this.nama = nama;
    this.no_hp = no_hp;
    this.token_listrik = token_listrik;
    this.tagihan_wifi = tagihan_wifi;
    this.catatan = catatan;
  }

  public RequestProfile(String nomor) {
    this.nomor = FormatUtils.normalizeNomor(nomor);
  }

  public void setCheckNomor(String nomor) {
    this.nomor = FormatUtils.normalizeNomor(nomor);
  }

  public RequestProfile setToken() {
    UserSession session = UserSession.with(activity);
    this.token = session.getToken();
    return this;
  }

  public void requestTambahPelanggan(CallbackTambahPelanggan callback) {
    ClientApi<ResponsePelanggan> requestDataOtp = new ClientApi<>(activity);
    Call<ResponsePelanggan> call =
        requestDataOtp
            .getEndpoint()
            .tambahPelanggan(token, nama, no_hp, token_listrik, tagihan_wifi, catatan);

    requestDataOtp.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponsePelanggan>() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(ResponsePelanggan data) {
            callback.onDataChanged(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public void requestGetPelanggan(CallbackGetPelanggan callback) {
    ClientApi<ResponsePelanggan> requestDataOtp = new ClientApi<>(activity);
    Call<ResponsePelanggan> call = requestDataOtp.getEndpoint().getPelanggan(token);
    requestDataOtp.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponsePelanggan>() {
          @Override
          public void onRequest() {
            callback.onConnect();
          }

          @Override
          public void onDataChange(ResponsePelanggan data) {
            callback.onProfile(data.getData());
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public interface CallbackCheckNomor {
    void onCheckNomor(ResponseProfile profile);

    void onFailure(String error);
  }

  public interface CallbackGetPelanggan {
    void onConnect();

    void onProfile(List<ResponsePelanggan.Data> profile);

    void onFailure(String error);
  }

  public interface CallbackTambahPelanggan {
    void onDataChanged(ResponsePelanggan pelanggan);

    void onFailure(String error);
  }

  public interface CallbackProfile {
    void onConnect();

    void onProfile(ResponseUsers profile);

    void onFailure(String error);
  }
}

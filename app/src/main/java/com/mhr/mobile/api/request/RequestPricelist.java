package com.mhr.mobile.api.request;

import android.app.Activity;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.listener.MokuListCallback;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.ui.intro.UserSession;
import java.util.List;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RequestPricelist {
  private Activity activity;
  private String token;
  private String tipe;
  private String kategori;
  private UserSession pref;
  private RequestBody rb;

  public RequestPricelist(Activity activity) {
    this.activity = activity;
    this.pref = UserSession.with(activity);
  }

  public RequestPricelist(String token, String tipe, String kategori) {
    this.token = token;
    this.tipe = tipe;
    this.kategori = kategori;
  }

  public RequestPricelist setType(String tipe) {
    this.token = pref.getToken();
    this.tipe = tipe;
    return this;
  }

  public RequestPricelist setKategori(String kategori) {
    this.kategori = kategori;
    return this;
  }

  public void startExecute(Callback callback) {
    // String jsonBody = new Gson().toJson(new RequestPricelist(token, tipe, kategori));
    // rb = rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    ClientApi<ResponsePricelist> requestData = new ClientApi<>(activity);
    Call<List<ResponsePricelist>> call = requestData.getEndpoint().getProduk(token, tipe, kategori);

    requestData.execute(
        call,
        new ClientApi.Callback<ResponsePricelist>() {
          @Override
          public void onRequest() {
            callback.onRequest();
          }

          @Override
          public void onDataChange(List<ResponsePricelist> data) {
            callback.onDataChange(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public void flashsaleAktif(MokuListCallback callback) {
    ClientApi<ResponsePricelist> api = new ClientApi<>(activity);
    Call<List<ResponsePricelist>> call = api.getEndpoint().getFlashsale();

    api.execute(
        call,
        new ClientApi.Callback<ResponsePricelist>() {
          @Override
          public void onRequest() {
            callback.onEvent();
          }

          @Override
          public void onDataChange(List<ResponsePricelist> data) {
            callback.onDataValue(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onError(errorMessage);
          }
        });
  }

  public interface Callback {
    void onRequest();

    void onDataChange(List<ResponsePricelist> pricelist);

    void onFailure(String errorMessage);
  }

  public interface CallbackRefresh {
    void onDataRefresh();

    void onRefreshFailed(String errorMessage);
  }
}

package com.mhr.mobile.api.request;

import android.app.Activity;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.response.ResponseHistory;
import java.util.List;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RequestHistory {
  private final Activity activity;
  private RequestBody rb;
  private ClientApi<ResponseHistory> request;
  private Call<ResponseHistory> call;
  private String userId;
  private String refId;
  private String provider;
  private String jenis;
  private int nominal;
  private String status;
  private String token;
  private String nomor;
  private int page;
  private int limit;
  private String kategori;

  public RequestHistory(Activity activity) {
    this.activity = activity;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setRefId(String refId) {
    this.refId = refId;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public void setJenis(String jenis) {
    this.jenis = jenis;
  }

  public void setNominal(int nominal) {
    this.nominal = nominal;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setNomor(String nomor) {
    this.nomor = nomor;
  }

  public void setPageLimit(int page, int limit) {
    this.page = page;
    this.limit = limit;
  }

  public void setKategori(String kategori) {
    this.kategori = kategori;
  }
  
  public void requestHistory(CallbackHistoryTransaksi callback) {
    request = new ClientApi<>(activity);
    call = request.getEndpoint().getTransaksiHistory(token, page, limit, kategori);

    request.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseHistory>() {
          @Override
          public void onRequest() {
            callback.sendRequest();
          }

          @Override
          public void onDataChange(ResponseHistory data) {
            callback.onHistory(data.getData().getItems());
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public void requestLastNumber(CallbackHistory callback) {
    ClientApi<ResponseHistory> request = new ClientApi<>(activity);
    Call<ResponseHistory> call = request.getEndpoint().getLastNumber(token, kategori,jenis);  

    request.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseHistory>() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(ResponseHistory data) {
            callback.onHistory(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public interface CallbackHistoryTransaksi {
    void sendRequest();

    void onHistory(List<ResponseHistory.Data> data);

    void onFailure(String error);
  }

  public interface CallbackHistory {
    void sendRequest();

    void onHistory(ResponseHistory history);

    void onFailure(String error);
  }
}

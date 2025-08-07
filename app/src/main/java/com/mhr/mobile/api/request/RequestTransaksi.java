package com.mhr.mobile.api.request;

import android.app.Activity;
import com.google.gson.Gson;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.response.ResponseTransaksi;
import com.mhr.mobile.api.response.ResponseTransaksiPasca;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RequestTransaksi {
  private Activity activity;
  private RequestBody rb;
  private String token;
  private String sku;
  private String ref_id;
  private String nama_pembeli;
  private String customer_no;
  private int price;
  private boolean testing;

  public RequestTransaksi(Activity activity) {
    this.activity = activity;
  }

  public RequestTransaksi(
      String token,
      String sku,
      String nama_pembeli,
      String customer_no,
      String ref_id,
      int price,
      boolean testing) {
    this.token = token;
    this.sku = sku;
    this.nama_pembeli = nama_pembeli;
    this.customer_no = customer_no;
    this.ref_id = ref_id;
    this.price = price;
    this.testing = testing;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public void setNamaPembeli(String nama_pembeli) {
    this.nama_pembeli = nama_pembeli;
  }

  public void setCustomerNo(String customer_no) {
    this.customer_no = customer_no;
  }

  public void setRefId(String ref_id) {
    this.ref_id = ref_id;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public void setTesting(boolean testing) {
    this.testing = testing;
  }

  public void requestTransaksi(Callback callback) {
    Map<String, Object> map = new HashMap<>();
    map.put("token", token);
    map.put("sku", sku);
    map.put("nama_pembeli", nama_pembeli);
    map.put("customer_no", customer_no);
    map.put("price", price);
    map.put("testing", testing);

    String jsonBody = new Gson().toJson(map);
    rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    ClientApi<ResponseTransaksi> clientApi = new ClientApi<>(activity);
    Call<ResponseTransaksi> call = clientApi.getEndpoint().getTransaksi(rb);

    clientApi.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseTransaksi>() {
          @Override
          public void onRequest() {
            callback.onRequest();
          }

          @Override
          public void onDataChange(ResponseTransaksi data) {
            callback.onDataChanged(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public void requestTransaksiPasca(CallbackPasca callback) {
    Map<String, Object> map = new HashMap<>();
    map.put("token", token);
    map.put("sku", sku);
    map.put("nama_pembeli", nama_pembeli);
    map.put("customer_no", customer_no);
    map.put("ref_id", ref_id);
    map.put("price", price);
    map.put("testing", testing);

    String jsonBody = new Gson().toJson(map);
    rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    ClientApi<ResponseTransaksiPasca> clientApi = new ClientApi<>(activity);
    Call<ResponseTransaksiPasca> call = clientApi.getEndpoint().getTransaksiPasca(rb);

    clientApi.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseTransaksiPasca>() {
          @Override
          public void onRequest() {
            callback.onRequest();
          }

          @Override
          public void onDataChange(ResponseTransaksiPasca data) {
            callback.onDataChanged(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public void requestCekStatus(Callback callback) {
    ClientApi<ResponseTransaksi> clientApi = new ClientApi<>(activity);
    Call<ResponseTransaksi> call = clientApi.getEndpoint().getCekStatus(ref_id);

    clientApi.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseTransaksi>() {
          @Override
          public void onRequest() {
            callback.onRequest();
          }

          @Override
          public void onDataChange(ResponseTransaksi data) {
            callback.onDataChanged(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public interface Callback {
    public void onRequest();

    public void onDataChanged(ResponseTransaksi response);

    public void onFailure(String error);
  }

  public interface CallbackPasca {
    public void onRequest();

    public void onDataChanged(ResponseTransaksiPasca response);

    public void onFailure(String error);
  }
}

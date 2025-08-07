package com.mhr.mobile.api.request.duitku;

import android.app.Activity;
import com.google.gson.Gson;
import com.mhr.mobile.api.client.ClientApi;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class DuitkuRequest {
  private Activity activity;
  private RequestBody rb;
  private int amount;
  private String paymentMethod;
  private String paymentName;
  private String customerName;
  private String phoneNumber;
  private String customerEmail;
  private String detail;
  private String biayaAdmin;
  private int kodeUnik;

  public DuitkuRequest(Activity activity) {
    this.activity = activity;
  }

  public DuitkuRequest(int amount) {
    this.amount = amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public void setPaymentName(String paymentName) {
    this.paymentName = paymentName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public void setCustomerPhone(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public void setBiayaAdmin(String biayaAdmin) {
    this.biayaAdmin = biayaAdmin;
  }

  public void setKodeUnik(int kodeUnik) {
    this.kodeUnik = kodeUnik;
  }

  public void requestPayment(Callback callback) {
    Map<String, Object> map = new HashMap<>();
    map.put("amount", amount);
    String jsonBody = new Gson().toJson(map);
    rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    ClientApi<DuitkuResponse> api = new ClientApi<>(activity);
    Call<DuitkuResponse> call = api.getEndpoint().getDuitku(rb);

    api.executeSingle(
        call,
        new ClientApi.CallbackSingle<DuitkuResponse>() {
          @Override
          public void onRequest() {
            callback.onPaymentRequest();
          }

          @Override
          public void onDataChange(DuitkuResponse data) {
            callback.onPaymentMethod(data.getPaymentMethod());
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onPaymentError(errorMessage);
          }
        });
  }

  public interface Callback {
    void onPaymentRequest();

    void onPaymentMethod(List<DuitkuResponse.PaymentMethod> method);

    void onPaymentError(String error);
  }

  public void requestTransaction(TransactionCallback callback) {
    Map<String, Object> map = new HashMap<>();
    map.put("amount", amount);
    map.put("paymentMethod", paymentMethod);
    map.put("paymentName", paymentName);
    map.put("customerName", customerName);
    map.put("phoneNumber", phoneNumber);
    map.put("customerEmail", customerEmail);
    map.put("detail", detail);
    map.put("biaya_admin", biayaAdmin);
	map.put("kode_unik", kodeUnik);

    String jsonBody = new Gson().toJson(map);
    rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);

    ClientApi<DuitkuResponse> api = new ClientApi<>(activity);
    Call<DuitkuResponse> call = api.getEndpoint().getDuitkuPayment(rb);

    api.executeSingle(
        call,
        new ClientApi.CallbackSingle<DuitkuResponse>() {
          @Override
          public void onRequest() {
            callback.onTransactionStart();
          }

          @Override
          public void onDataChange(DuitkuResponse data) {
            callback.onTransactionSuccess(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onTransactionFailed(errorMessage);
          }
        });
  }

  public interface TransactionCallback {
    void onTransactionStart();

    void onTransactionSuccess(DuitkuResponse data);

    void onTransactionFailed(String error);
  }
}

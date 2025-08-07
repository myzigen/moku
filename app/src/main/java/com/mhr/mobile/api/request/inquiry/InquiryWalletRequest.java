package com.mhr.mobile.api.request.inquiry;

import android.app.Activity;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.response.inquiry.WalletResponse;
import retrofit2.Call;

public class InquiryWalletRequest {
  private final Activity activity;
  private String nomor, nama, brand;

  public InquiryWalletRequest(Activity activity) {
    this.activity = activity;
  }

  public static InquiryWalletRequest with(Activity activity) {
    return new InquiryWalletRequest(activity);
  }

  public InquiryWalletRequest OnSave(String nomor, String nama, String brand) {
    this.nomor = nomor;
    this.nama = nama;
    this.brand = brand;
    return this;
  }

  public InquiryWalletRequest OnGet(String nomor) {
    this.nomor = nomor;
    return this;
  }

  public void SaveWalletData(Callback callback) {
    ClientApi<WalletResponse> api = new ClientApi<>(activity);
    Call<WalletResponse> call = api.getEndpoint().saveWalletData(nomor, nama, brand);
    api.executeSingle(
        call,
        new ClientApi.CallbackSingle<WalletResponse>() {
          @Override
          public void onRequest() {
            callback.onLoading();
          }

          @Override
          public void onDataChange(WalletResponse data) {
            callback.onDataCheck(data);
          }

          @Override
          public void onFailure(String errorMessage) {
			callback.onFailure(errorMessage);
		  }
        });
  }

  public void GetWalletData(Callback callback) {
    ClientApi<WalletResponse> api = new ClientApi<>(activity);
    Call<WalletResponse> call = api.getEndpoint().getWalletData(nomor);
    api.executeSingle(
        call,
        new ClientApi.CallbackSingle<WalletResponse>() {
          @Override
          public void onRequest() {
            callback.onLoading();
          }

          @Override
          public void onDataChange(WalletResponse data) {
            callback.onDataCheck(data);
          }

          @Override
          public void onFailure(String errorMessage) {
			  callback.onFailure(errorMessage);
		  }
        });
  }

  public interface Callback {
    void onLoading();

    void onDataCheck(WalletResponse response);

    void onFailure(String error);
  }
}

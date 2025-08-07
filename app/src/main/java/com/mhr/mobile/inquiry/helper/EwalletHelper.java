package com.mhr.mobile.inquiry.helper;

import android.app.Activity;
import com.mhr.mobile.api.request.inquiry.InquiryWalletRequest;
import com.mhr.mobile.api.response.inquiry.WalletResponse;
import com.mhr.mobile.inquiry.request.InquiryRequest;
import com.mhr.mobile.inquiry.response.InquiryResponse;
import com.mhr.mobile.util.AndroidViews;
import java.util.Map;

public class EwalletHelper {
  private InquiryWalletRequest api;
  private Activity context;

  public EwalletHelper(Activity context) {
    this.context = context;
    this.api = InquiryWalletRequest.with(context);
  }

  public void checkCustomerName(String nomor, String brand, int price, EwalletCallback callback) {
    api.OnGet(nomor);
    api.GetWalletData(
        new InquiryWalletRequest.Callback() {
          @Override
          public void onLoading() {
            callback.onLoading();
          }

          @Override
          public void onDataCheck(WalletResponse response) {
            WalletResponse.WalletData walletData = response.get(nomor);

            WalletResponse.BrandData brandData = null;

            if (walletData != null && walletData.getData() != null) {
              String normalizedInput = normalizeBrand(brand);
              for (Map.Entry<String, WalletResponse.BrandData> entry :
                  walletData.getData().entrySet()) {
                if (normalizeBrand(entry.getKey()).equals(normalizedInput)) {
                  brandData = entry.getValue();
                  break;
                }
              }

              if (brandData != null && brandData.getNama() != null) {
                InquiryResponse.EwalletData data = new InquiryResponse.EwalletData(nomor, brandData.getNama(), brand);
                callback.onDataFound(data);
                return;
              }
            }

            // jika data kosong atau tidak lengkap, panggil API
            fetchFromApiAndSave(nomor, price, brand, callback);
          }

          @Override
          public void onFailure(String error) {
            callback.onError(error);
          }
        });
  }

  private void fetchFromApiAndSave(String nomor, int price, String brand, EwalletCallback callback) {
    //AndroidViews.showToast("Panggil Api", context);
    callback.onLoading();
	String normalizedBrand = normalizeBrand(brand);
    InquiryRequest request = new InquiryRequest(context);
    request.setUsername();
    request.setApiKey();
    request.setCodeProduk(normalizedBrand);
    request.setHp(nomor);
    request.setAmount(price);

    request.startInquiryRequest(
        new InquiryRequest.InquiryCallback() {
          @Override
          public void onStartLoading() {
            callback.onLoading();
          }

          @Override
          public void onResponse(InquiryResponse response) {

            InquiryResponse.Data data = response.getData();
            if (data != null && data.getTrName() != null && !data.getTrName().isEmpty()) {
              String no = data.getHp();
              String nama = data.getTrName();
              String code = data.getCode();
              InquiryResponse.EwalletData resultData =
                  new InquiryResponse.EwalletData(no, nama, code);
              if (code.equalsIgnoreCase(normalizedBrand)) {
                saveToServer(nomor, resultData, normalizedBrand, callback);
                callback.onDataFound(resultData);
              }
            } else {
              callback.onError(" Tidak Diketahui");
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onError(errorMessage);
          }
        });
  }

  private void saveToServer(String no, InquiryResponse.EwalletData data, String brand, EwalletCallback callback) {
    api.OnSave(no, data.getTrName(), brand);
    api.SaveWalletData(
        new InquiryWalletRequest.Callback() {
          @Override
          public void onLoading() {
            callback.onLoading();
          }

          @Override
          public void onDataCheck(WalletResponse response) {
            callback.onDataSaved();
          }

          @Override
          public void onFailure(String error) {
            callback.onError(error);
          }
        });
  }

  private String normalizeBrand(String brand) {
    return brand.toLowerCase().replace(" ", "").replace("-", "").replace("_", "");
  }

  public interface EwalletCallback {
    void onLoading();

    void onDataFound(InquiryResponse.EwalletData data);

    void onDataSaved();

    void onError(String errorMessage);
  }
}

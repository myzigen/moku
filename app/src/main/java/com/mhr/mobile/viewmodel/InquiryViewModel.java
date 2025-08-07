package com.mhr.mobile.viewmodel;

import android.app.Activity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.database.DatabaseReference;
import com.mhr.mobile.inquiry.request.InquiryRequest;
import com.mhr.mobile.inquiry.response.InquiryPLNResponse;
import com.mhr.mobile.inquiry.response.InquiryResponse;

public class InquiryViewModel extends ViewModel {
  private MutableLiveData<InquiryResponse.Data> mData = new MutableLiveData<>();
  private MutableLiveData<InquiryResponse.WifiData> mDataWifi = new MutableLiveData<>();
  private MutableLiveData<InquiryPLNResponse.Data> mPlnData = new MutableLiveData<>();
  private MutableLiveData<InquiryPLNResponse.SaveData> mPlnDataSaved = new MutableLiveData<>();
  private MutableLiveData<String> mErrorMessage = new MutableLiveData<>();

  // Getters for LiveData
  public LiveData<InquiryResponse.Data> getInquiryData() {
    return mData;
  }

  public LiveData<InquiryResponse.WifiData> getWifiData() {
    return mDataWifi;
  }

  public LiveData<String> getErrorMessage() {
    return mErrorMessage;
  }

  // Fetch Wifi data from Firebase
  public void checkCustomerName(
      Activity a,
      String noPelanggan,
      String selectedCodeProduk,
      DatabaseReference db,
      InquiryCallback callback) {
    callback.onLoading();
    db.child(noPelanggan)
        .get()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful() && task.getResult().exists()) {
                InquiryResponse.WifiData wifiData =
                    task.getResult().getValue(InquiryResponse.WifiData.class);
                mDataWifi.setValue(wifiData);
                if (wifiData != null) {
                  callback.onDataApi(wifiData);
                } else {
                  fetchFromApiAndSaveToFirebase(a, noPelanggan, selectedCodeProduk, db, callback);
                }
              } else {
                // Jika data tidak ada di Firebase, panggil API untuk mendapatkan data
                mErrorMessage.setValue("Data tidak ditemukan di Firebase, mengambil dari API...");
                fetchFromApiAndSaveToFirebase(a, noPelanggan, selectedCodeProduk, db, callback);
              }
            });
  }

  // Fetch data from API
  public void fetchFromApiAndSaveToFirebase(
      Activity activity,
      String noPelanggan,
      String selectedCodeProduk,
      DatabaseReference db,
      InquiryCallback callback) {
    callback.onLoading();
    InquiryRequest request = new InquiryRequest(activity);
    request.setUsername();
    request.setApiKey();
    request.setCodeProduk(selectedCodeProduk);
    request.setHp(noPelanggan);

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
			  callback.onDataApi(data);
              // Simpan data ke Firebase setelah mendapatkan data dari API
              InquiryResponse.WifiData wifiData = new InquiryResponse.WifiData(data.getHp(), data.getTrName());
			  mData.setValue(data);
			  mDataWifi.setValue(wifiData);
              saveToFirebase(noPelanggan, wifiData, db);
            } else {
              String message = data != null ? data.getMessage() : "Nama pengguna tidak ditemukan";
              mErrorMessage.setValue(message);
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            mErrorMessage.setValue("Gagal mengambil data: " + errorMessage);
          }
        });
  }

  // Simpan data ke Firebase
  private void saveToFirebase(
      String noPelanggan, InquiryResponse.WifiData wifiData, DatabaseReference db) {
    db.child(noPelanggan)
        .setValue(wifiData)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                // mErrorMessage.setValue("Data berhasil disimpan di Firebase.");
              } else {
                mErrorMessage.setValue("Gagal menyimpan data ke Firebase.");
              }
            })
        .addOnFailureListener(
            e -> mErrorMessage.setValue("Error menyimpan data ke Firebase: " + e.getMessage()));
  }

  public interface InquiryCallback {
    void onLoading();

    void onDataApi(Object data);
  }
}

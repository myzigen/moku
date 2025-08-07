package com.mhr.mobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatusTransaksiViewModel extends ViewModel {
  private MutableLiveData<String> customerNoLiveData = new MutableLiveData<>();
  private MutableLiveData<String> refIdLiveData = new MutableLiveData<>();

  public LiveData<String> getCustomerNo() {
    return customerNoLiveData;
  }

  public LiveData<String> getRefId() {
    return refIdLiveData;
  }

  public void setViewModel(String nomor, String refId) {
    customerNoLiveData.setValue(nomor);
    refIdLiveData.setValue(refId);
  }
}

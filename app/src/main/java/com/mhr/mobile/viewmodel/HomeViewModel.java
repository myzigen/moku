package com.mhr.mobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.mhr.mobile.model.MenuKategoriModel;
import java.util.List;

public class HomeViewModel extends ViewModel {
  private final MutableLiveData<Integer> badgeCount = new MutableLiveData<>(0);
  private final MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>(false);
  private final MutableLiveData<List<MenuKategoriModel>> kategori = new MutableLiveData<>();
  private final MutableLiveData<String> saldoLiveData = new MutableLiveData<>();

  public LiveData<Integer> getBadgeCount() {
    return badgeCount;
  }

  public void incrementBadgeCount() {
    Integer current = badgeCount.getValue() != null ? badgeCount.getValue() : 0;
    badgeCount.setValue(current + 1);
  }

  public void clearBadgeCount() {
    badgeCount.setValue(0);
  }

  public LiveData<String> getSaldo() {
    return saldoLiveData;
  }

  public void setSaldo(String saldo) {
    saldoLiveData.setValue(saldo);
  }

  public LiveData<Boolean> getIsRefreshing() {
    return isRefreshing;
  }

  public void setIsRefreshing(Boolean refreshing) {
    isRefreshing.setValue(refreshing);
  }

  public interface RequestData {
    void onStartLoading();

    void onSucess(List<?> data);
  }
}

/*
public class HomeViewModel extends ViewModel {

}
*/

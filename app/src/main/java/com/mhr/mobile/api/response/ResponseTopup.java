package com.mhr.mobile.api.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResponseTopup {
  private boolean status;
  private String nomor;

  @SerializedName("data")
  private List<TopupItem> data;

  public List<TopupItem> getData() {
    return data;
  }

  public static class TopupItem {
    @SerializedName("jumlah_topup")
    private int jumlah_topup;

    @SerializedName("kode_unik")
    private int kode_unik;

    @SerializedName("total_topup")
    private int total_topup;

    public int getJumlahTopup() {
      return jumlah_topup;
    }

    public int getKodeUnik() {
      return kode_unik;
    }

    public int getTotalTopup() {
      return total_topup;
    }
  }
}

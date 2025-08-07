package com.mhr.mobile.api.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// ResponsePelanggan.java
public class ResponsePelanggan {
  private boolean status;
  private String message;

  @SerializedName("data")
  private List<Data> data;

  public boolean isStatus() {
    return status;
  }

  public List<Data> getData() {
    return data;
  }

  public static class Data {
    private String id;

    @SerializedName("nama")
    private String nama;

    @SerializedName("no_hp")
    private String noHp;

    private String catatan;

    public String getId() {
      return id;
    }

    public String getNama() {
      return nama;
    }

    public String getNoHp() {
      return noHp;
    }

    public String getCatatan() {
      return catatan;
    }
  }
}

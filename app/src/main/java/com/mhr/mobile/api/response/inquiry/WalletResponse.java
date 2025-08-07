package com.mhr.mobile.api.response.inquiry;

import java.util.Map;

public class WalletResponse extends java.util.HashMap<String, WalletResponse.WalletData> {

  public static class WalletData {
    private Map<String, BrandData> data;

    public Map<String, BrandData> getData() {
      return data;
    }

    public void setData(Map<String, BrandData> data) {
      this.data = data;
    }
  }

  public static class BrandData {
    private String nama;

    public String getNama() {
      return nama;
    }

    public void setNama(String nama) {
      this.nama = nama;
    }
  }
}

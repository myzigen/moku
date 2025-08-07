package com.mhr.mobile.model;

import com.mhr.mobile.api.response.ResponseHistory;

public class RiwayatHeader {
  public static final int TYPE_HEADER = 0;
  public static final int TYPE_ITEM = 1;
  private int type;
  private String headerDate;
  public ResponseHistory.Data data;

  public RiwayatHeader(String headerDate) {
    this.type = TYPE_HEADER;
    this.headerDate = headerDate;
  }

  public RiwayatHeader(ResponseHistory.Data data) {
    this.type = TYPE_ITEM;
    this.data = data;
  }

  public int getType() {
    return type;
  }

  public String getHeaderDate() {
    return headerDate;
  }

  public ResponseHistory.Data getData() {
    return data;
  }

  private long totalJumlah = 0;

  public void setTotalJumlah(long totalJumlah) {
    this.totalJumlah = totalJumlah;
  }

  public long getTotalJumlah() {
    return totalJumlah;
  }
}

package com.mhr.mobile.model;

public class GabungkanHistory {
  public enum Source {
    DIGIFLAZZ,
    TRIPAY
  }

  private Source source;
  private String produk;
  private String customerNo;
  private String status;
  private int harga;
  private long timestamp;

  // Constructor, Getter dan Setter
  public GabungkanHistory(
      Source source,
      String produk,
      String customerNo,
      String status,
      int harga,
      long timestamp) {
    this.source = source;
    this.produk = produk;
    this.customerNo = customerNo;
    this.status = status;
    this.harga = harga;
    this.timestamp = timestamp;
  }

  public Source getSource() {
    return source;
  }

  public String getProduk() {
    return produk;
  }

  public String getCustomerNo() {
    return customerNo;
  }

  public String getStatus() {
    return status;
  }

  public int getHarga() {
    return harga;
  }

  public long getTimestamp() {
    return timestamp;
  }
}

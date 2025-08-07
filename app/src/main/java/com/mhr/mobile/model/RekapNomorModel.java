package com.mhr.mobile.model;

public class RekapNomorModel {
  private String customer_no;
  private int total_pembelian;

  // Getter & Setter

  public RekapNomorModel(String customer_no, int total) {
    this.customer_no = customer_no;
    this.total_pembelian = total;
  }

  public String getCustomerNo() {
    return customer_no;
  }

  public int getTotalPembelian() {
    return total_pembelian;
  }
}

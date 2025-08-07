package com.mhr.mobile.api.response;

public class ResponseTransaksi {
  public boolean status;
  public String message;
  public Data data;

  public boolean isStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public Data getData() {
    return data;
  }

  public static class Data {
    public String from; // "server" atau "digiflazz"
    public String type; // "saldo", "token_tidak_valid", "input_kurang", dll
    public int expected; // jika type saldo
    public int available; // jika type saldo
    public String ref_id;
    public String customer_no;
    public String buyer_sku_code;
    public String sn;
    public String tele;
    public String wa;
    public int price;
    public String status;
    public String message;

    public String getCustomerNo() {
      return customer_no;
    }

    public String getSn() {
      return sn;
    }

    public String getFrom() {
      return from;
    }

    public String getType() {
      return type;
    }

    public int getExpected() {
      return expected;
    }

    public int getAvailable() {
      return available;
    }

    public String getRefId() {
      return ref_id;
    }

    public void setRefId(String ref_id) {
      this.ref_id = ref_id;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }
  }
}

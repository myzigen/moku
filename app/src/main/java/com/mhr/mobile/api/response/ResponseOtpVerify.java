package com.mhr.mobile.api.response;

public class ResponseOtpVerify {
  private String status;
  private String message;
  private Data data;

  public static class Data {
    private String nomor;
    private String nama;
    private String token;
    private String sign;
    private String pin;
    private int saldo;
    private String referral_code;
    private String upline_code;

    // Getter & Setter
  }

  // Getter & Setter
}

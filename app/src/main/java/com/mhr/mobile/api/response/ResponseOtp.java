package com.mhr.mobile.api.response;

public class ResponseOtp {
  public String status;
  public String message;
  public Data data;

  public static class Data {
    public String nomor;
  }
}

package com.mhr.mobile.api.response;

public class ResponseRegister {

  private String status;
  private String message;
  public String upline_nama;
  public Data data;

  public String getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public static class Data {
    public String token;
	public String nama;
	public String nomor;
  }
}

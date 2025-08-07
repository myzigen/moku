package com.mhr.mobile.api.response;

public class ResponseLogin {
  private String status;
  private String message;
  private long kadaluwarsa;
  private User data;

  public String getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
  
  public long getKadaluwarsa(){
	  return kadaluwarsa;
  }

  public User getData() {
    return data;
  }

  public static class User {
    private String nomor;
    private String nama;
    private String token;
	private String pin;
    private int saldo;

    public String getNama() {
      return nama;
    }

    public void setNama(String nama) {
      this.nama = nama;
    }

    public String getNomor() {
      return nomor;
    }
	
	public String getPin(){
		return pin;
	}

    public String getToken() {
      return token;
    }

    public int getSaldo() {
      return saldo;
    }
  }
}

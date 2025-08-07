package com.mhr.mobile.api.response;

public class ResponseProfile {
  private String status;
  private String message;
  private Data data;

  public static class Data {
    private String nomor;
    private String nama;
	private String referral_code;
    private int saldo;

    // Getter
    public String getNomor() {
      return nomor;
    }

    public String getNama() {
      return nama;
    }

    public int getSaldo() {
      return saldo;
    }
	
	public String getReferralCode(){
		return referral_code;
	}
  }

  public String getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public Data getData() {
    return data;
  }
}

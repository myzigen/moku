package com.mhr.mobile.model;

public class Bank {
  private String bankName;
  private String codeProduk;
  private String noRekening;
  private String type;
  private String logoUrl;

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }
  
  public String getCodeProduk(){
	  return codeProduk;
  }
  
  public void setCodeProduk(String codeProduk){
	  this.codeProduk = codeProduk;
  }

  public String getNoRekening() {
    return noRekening;
  }

  public void setNoRekening(String noRekening) {
    this.noRekening = noRekening;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public static class BankHeader {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private int type; // Jenis (Header atau Item)
    private String headerTitle; // Judul Header
    private Bank bank; // Data Bank

    public BankHeader(int type, String headerTitle, Bank bank) {
      this.type = type;
      this.headerTitle = headerTitle;
      this.bank = bank;
    }

    public int getType() {
      return type;
    }

    public String getHeaderTitle() {
      return headerTitle;
    }

    public Bank getBank() {
      return bank;
    }
  }
}

package com.mhr.mobile.api.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResponseUsers {
  private String status;
  private String message;
  private String upline_nama;
  public boolean has_pin;
  private long kadaluwarsa;

  @SerializedName("expired_at")
  private long expired_at;

  @SerializedName("data")
  public User data;

  public String getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public String getUplineNama() {
    return upline_nama;
  }

  public long getKadaluwarsa() {
    return kadaluwarsa;
  }

  public long getExpiredAt() {
    return expired_at;
  }

  public User getData() {
    return data;
  }

  public static class User {

    @SerializedName("nomor")
    private String nomor;

    @SerializedName("nama")
    private String nama;

    @SerializedName("token")
    private String token;

    @SerializedName("hash_pin")
    private boolean hash_pin;

    @SerializedName("pin")
    private String pin;

    @SerializedName("referral_code")
    private String referral_code;

    @SerializedName("upline_code")
    private String upline_code;

    @SerializedName("nama_toko")
    private String nama_toko;

    @SerializedName("alamat_toko")
    private String alamat_toko;

    @SerializedName("total_downline")
    public int total_downline;

    @SerializedName("downline")
    private List<Downline> downlineList;

    @SerializedName("saldo")
    private int saldo;

    public User() {}

    public User(
        String token,
        String nama,
        String nomor,
        String nama_toko,
        String alamat_toko,
        boolean hash_pin) {
      this.token = token;
      this.nama = nama;
      this.nomor = nomor;
      this.nama_toko = nama_toko;
      this.alamat_toko = alamat_toko;
      this.hash_pin = hash_pin;
    }

    public List<Downline> getDownline() {
      return downlineList;
    }

    public String getReferralCode() {
      return referral_code;
    }

    public String getUplineCode() {
      return upline_code;
    }

    public String getNama() {
      return nama;
    }

    public void setNama(String nama) {
      this.nama = nama;
    }

    public String getNamaToko() {
      return nama_toko;
    }

    public void setNamaToko(String nama_toko) {
      this.nama_toko = nama_toko;
    }

    public String getAlamatToko() {
      return alamat_toko;
    }

    public void setAlamatToko(String alamat_toko) {
      this.alamat_toko = alamat_toko;
    }

    public String getNomor() {
      return nomor;
    }

    public void setNomor(String nomor) {
      this.nomor = nomor;
    }

    public boolean getHashPin() {
      return hash_pin;
    }

    public void setHashPin(boolean hash_pin) {
      this.hash_pin = hash_pin;
    }

    public String getPin() {
      return pin;
    }

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }

    public int getSaldo() {
      return saldo;
    }
  }

  public static class Downline {
    private String nama;
    private String nomor;
    private String created_at;

    public String getNama() {
      return nama;
    }

    public String getNomor() {
      return nomor;
    }

    public String getCreatedAt() {
      return created_at;
    }
  }
}

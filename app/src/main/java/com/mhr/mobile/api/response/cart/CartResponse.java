package com.mhr.mobile.api.response.cart;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CartResponse {
  public String status;
  public String message;

  @SerializedName("cart")
  public List<CartItem> cart;

  public static class CartItem {
    @SerializedName("id")
    public int id;

    @SerializedName("token")
    public String token;

    @SerializedName("nama_produk")
    public String nama_produk;

    @SerializedName("kode_produk")
    public String kode_produk;

    @SerializedName("brand_icon_url")
    public String brand_icon_url;

    @SerializedName("harga")
    public int harga;

    @SerializedName("nomor_tujuan")
    public String nomor_tujuan;

    @SerializedName("created_at")
    public String created_at;
  }
}

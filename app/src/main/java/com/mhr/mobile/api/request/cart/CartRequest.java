package com.mhr.mobile.api.request.cart;

import android.app.Activity;
import com.mhr.mobile.MyApp;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.response.cart.CartResponse;
import com.mhr.mobile.ui.intro.UserSession;
import com.mhr.mobile.util.QiosPreferences;
import retrofit2.Call;

public class CartRequest {
  private Activity activity;
  private UserSession pref;
  private String nama_produk;
  private String kode_produk;
  private String nomor_tujuan;
  private String brand_icon_url;
  private int harga;

  public CartRequest(Activity activity) {
    this.activity = activity;
    pref = UserSession.with(activity);
  }

  public void setNamaProduk(String nama_produk) {
    this.nama_produk = nama_produk;
  }

  public void setCodeProduk(String kode_produk) {
    this.kode_produk = kode_produk;
  }

  public void setNomorTujuan(String nomor_tujuan) {
    this.nomor_tujuan = nomor_tujuan;
  }

  public void setHarga(int harga) {
    this.harga = harga;
  }

  public void setBrandIcon(String brand_icon_url) {
    this.brand_icon_url = brand_icon_url;
  }

  public void requestAddToCart(Callback callback) {
    CartResponse.CartItem item = new CartResponse.CartItem();
    item.token = pref.getToken();
    item.nama_produk = nama_produk;
    item.kode_produk = kode_produk;
    item.nomor_tujuan = nomor_tujuan;
    item.brand_icon_url = brand_icon_url;
    item.harga = harga;

    ClientApi<CartResponse> api = new ClientApi<>(activity);
    Call<CartResponse> call = api.getEndpoint().addCartItem(item);
    api.executeSingle(
        call,
        new ClientApi.CallbackSingle<CartResponse>() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(CartResponse data) {
            callback.onAddToCart(data);
            int newCartSize = data.cart != null ? data.cart.size() : 0;
            MyApp.notifyCartUpdate(newCartSize);
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
  }

  public void requestGetCart(Callback callback) {
    String token = pref.getToken();
    ClientApi<CartResponse> api = new ClientApi<>(activity);
    Call<CartResponse> call = api.getEndpoint().getCartItem(token);
    api.executeSingle(
        call,
        new ClientApi.CallbackSingle<CartResponse>() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(CartResponse data) {
            callback.onAddToCart(data);
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
  }

  public void requestDeleteCart(int id, CartDeleteCallback callback) {
    CartResponse.CartItem item = new CartResponse.CartItem();
    item.token = pref.getToken();
    item.id = id;

    ClientApi<CartResponse> api = new ClientApi<>(activity);
    Call<CartResponse> call = api.getEndpoint().deleteCartItem(item);

    api.executeSingle(
        call,
        new ClientApi.CallbackSingle<CartResponse>() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(CartResponse data) {
            callback.onDeleted(data);
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
  }

  public interface CartDeleteCallback {
    void onDeleted(CartResponse response);
  }

  public interface Callback {
    void onAddToCart(CartResponse response);
  }
}

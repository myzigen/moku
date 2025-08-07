package com.mhr.mobile.ui.produk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.DataInternetAdapter;
import com.mhr.mobile.api.response.ResponseHistory;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.QiosColor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterHelper {
  // * Menghilanglan Duplikat Produk untuk menampilkan daftar
  @NonNull
  public static List<ResponsePricelist> removeDuplicate(List<ResponsePricelist> remove) {
    Map<String, ResponsePricelist> uniqueProduk = new HashMap<>();
    for (ResponsePricelist pricelist : remove) {
      String key = pricelist.getBrand();
      uniqueProduk.put(key, pricelist);
    }
    return new ArrayList<>(uniqueProduk.values());
  }

  @NonNull // * Menghilanglan Duplikat Produk berdasarkan sku
  public static List<ResponseHistory.Data> removeDuplicateSku(List<ResponseHistory.Data> remove) {
    Map<String, ResponseHistory.Data> uniqueProduk = new HashMap<>();
    for (ResponseHistory.Data pricelist : remove) {
      String key = pricelist.getRefId();
      uniqueProduk.put(key, pricelist);
    }
    return new ArrayList<>(uniqueProduk.values());
  }

  @NonNull // * Menghilanglan Duplikat Produk berdasarkan Type
  public static List<ResponsePricelist> removeDuplicateType(List<ResponsePricelist> remove) {
    Map<String, ResponsePricelist> uniqueProduk = new HashMap<>();
    for (ResponsePricelist pricelist : remove) {
      String key = pricelist.getType();
      uniqueProduk.put(key, pricelist);
    }
    return new ArrayList<>(uniqueProduk.values());
  }

  @NonNull // * Menghilanglan Duplikat Produk berdasarkan Type
  public static List<ResponsePricelist> removeDuplicateMasaAktif(List<ResponsePricelist> remove) {
    Map<String, ResponsePricelist> uniqueProduk = new HashMap<>();
    for (ResponsePricelist pricelist : remove) {
      String key = pricelist.getMasaAktif();
      uniqueProduk.put(key, pricelist);
    }
    return new ArrayList<>(uniqueProduk.values());
  }

  // * urutkan adapter berdasarkan harga terendah
  @NonNull
  public static void sortProductListByPrice(List<ResponsePricelist> mData) {
    if (mData != null) {
      Collections.sort(mData, (p1, p2) -> Integer.compare(p1.getHargaJual(), p2.getHargaJual()));
    }
  }

  public static void hitungJumlahProdukByKuota(DataInternetAdapter adapter, TextView text) {
    int jumlah = adapter.getItemCount();
    text.setText(jumlah + "Produk");
  }

  public static void setStatusColor(TextView textView, String status, Context mContext) {
    if (status == null) return;
    switch (status.toLowerCase()) {
      case "sukses":
      case "paid":
	    textView.setBackground(AndroidViews.getDrawable(mContext,R.drawable.corners_bg_success));
        textView.setTextColor(QiosColor.getColor(mContext, R.color.status_approved));
        textView.setText("SUKSES");
        break;
      case "pending":
      case "unpaid":
        textView.setBackground(AndroidViews.getDrawable(mContext, R.drawable.corners_bg_pending));
        textView.setTextColor(QiosColor.getColor(mContext, R.color.color_pending_front));
        textView.setText(status.equalsIgnoreCase("pending") ? "Di PROSES" : "Di PROSES");
        break;
      case "gagal":
      case "failed":
        textView.setTextColor(QiosColor.getColor(mContext, R.color.status_canceled));
        textView.setText("GAGAL");
        textView.setBackground(AndroidViews.getDrawable(mContext,R.drawable.corners_bg_error));
        break;
      default:
        textView.setTextColor(QiosColor.getColor(mContext, R.color.status_pending));
        textView.setText(status);
        break;
    }
  }
}

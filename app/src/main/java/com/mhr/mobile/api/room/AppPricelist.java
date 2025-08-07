package com.mhr.mobile.api.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pricelist")
public class AppPricelist {
  @PrimaryKey
  @NonNull
  public String kode_produk;
  public String nama_produk;
  public int harga_produk;
  public String type;
  public String kategori;
}

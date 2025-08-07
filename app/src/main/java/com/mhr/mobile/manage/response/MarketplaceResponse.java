package com.mhr.mobile.manage.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MarketplaceResponse {
  @SerializedName("data")
  private List<Data> data;

  public List<Data> getData() {
    return data;
  }

  public static class Data implements Parcelable {
    @SerializedName("nama_produk")
    private String produkName;

    @SerializedName("harga_produk")
    private int harga;

    @SerializedName("deskripsi_panjang")
    private String deskripsi;

    @SerializedName("list_gambar")
    private List<String> imageUrl;

    public String getProdukName() {
      return produkName;
    }

    public int getHarga() {
      return harga;
    }

    public List<String> getImageUrl() {
      return imageUrl;
    }
	
	public String getDeskripsi(){
		return deskripsi;
	}

    // Constructor Parcelable
    protected Data(Parcel in) {
      produkName = in.readString();
      harga = in.readInt();
      imageUrl = in.createStringArrayList();
	  deskripsi = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(produkName);
      dest.writeInt(harga);
      dest.writeStringList(imageUrl);
	  dest.writeString(deskripsi);
    }

    @Override
    public int describeContents() {
      return 0;
    }

    public static final Creator<Data> CREATOR =
        new Creator<Data>() {
          @Override
          public Data createFromParcel(Parcel in) {
            return new Data(in);
          }

          @Override
          public Data[] newArray(int size) {
            return new Data[size];
          }
        };
  }
}

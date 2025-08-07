package com.mhr.mobile.api.response;

import android.os.Parcel;
import android.os.Parcelable;

public class ResponseInquiryPasca {

  public boolean status;
  public String message;
  public Data data;

  public static class Data implements Parcelable {
    public String ref_id;
    public String customer_no;
    public String customer_name;
    public String buyer_sku_code;
    public int admin;
    public String message;
    public String status;
    public String rc;
    public String sn;
    public float buyer_last_saldo;
    public int price;
    public int selling_price;
    public String tarif;
    public int daya;

    // Ambil dari file Desc.java
    public Desc desc;

    protected Data(Parcel in) {
      ref_id = in.readString();
      customer_no = in.readString();
      customer_name = in.readString();
      buyer_sku_code = in.readString();
      admin = in.readInt();
      message = in.readString();
      status = in.readString();
      rc = in.readString();
      sn = in.readString();
      buyer_last_saldo = in.readFloat();
      price = in.readInt();
      selling_price = in.readInt();
      tarif = in.readString();
      daya = in.readInt();
      desc = in.readParcelable(Desc.class.getClassLoader());
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(ref_id);
      dest.writeString(customer_no);
      dest.writeString(customer_name);
      dest.writeString(buyer_sku_code);
      dest.writeInt(admin);
      dest.writeString(message);
      dest.writeString(status);
      dest.writeString(rc);
      dest.writeString(sn);
      dest.writeFloat(buyer_last_saldo);
      dest.writeInt(price);
      dest.writeInt(selling_price);
      dest.writeString(tarif);
      dest.writeInt(daya);
      dest.writeParcelable(desc, flags);
    }

    @Override
    public int describeContents() {
      return 0;
    }
  }
}

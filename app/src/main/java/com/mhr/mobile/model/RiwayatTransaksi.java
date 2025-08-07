package com.mhr.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RiwayatTransaksi implements Parcelable {
  private String type_api;
  private String ref_id;
  private int tr_id;
  private String rc;
  private String sn;
  private String status;
  private String statusTransaksiMidtrans; // Midtrans
  private String message;
  private String updateStatusMessage;
  private double harga;
  private long timestamp;
  private String customer_id;
  private String product_code;
  private String brand;
  private int iconResId;
  private String imageUrl;
  private String transactionTime; // Midtrans
  private long expiryDuration; // Midtrans
  private String expiryTime; // Midtrans
  private String merchantName; // Midtrans
  private String kategori;
  private String callbackUrl;

  public RiwayatTransaksi() {
    // Firebase
  }

  public RiwayatTransaksi(
      String type_api,
      String ref_id,
      int tr_id,
      String status,
      String message,
      String updateStatusMessage,
      double harga,
      long timestamp,
      String customer_id,
      String product_code,
      String brand,
      int iconResId,
      String imageUrl) {
    this.type_api = type_api;
    this.ref_id = ref_id;
    this.tr_id = tr_id;
    this.status = status;
    this.message = message;
    this.updateStatusMessage = updateStatusMessage;
    this.harga = harga;
    this.timestamp = timestamp;
    this.customer_id = customer_id;
    this.product_code = product_code;
    this.brand = brand;
    this.iconResId = iconResId;
    this.imageUrl = imageUrl;
  }

  // Getter dan Setter

  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }

  public String getKategori() {
    return kategori;
  }

  public void setKategori(String kategori) {
    this.kategori = kategori;
  }

  public String getTypeApi() {
    return type_api;
  }

  public void setTypeApi(String type_api) {
    this.type_api = type_api;
  }

  public String getRefId() {
    return ref_id;
  }

  public void setRefId(String ref_id) {
    this.ref_id = ref_id;
  }

  public String getRc() {
    return rc;
  }

  public void setRc(String rc) {
    this.rc = rc;
  }

  public String getSn() {
    return sn;
  }

  public void setSn(String sn) {
    this.sn = sn;
  }

  public int getTrId() {
    return tr_id;
  }

  public void setTrId(int tr_id) {
    this.tr_id = tr_id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
  // Midtrans
  public String getStatusTransaksiMidtrans() {
    return statusTransaksiMidtrans;
  }
  // Midtrans
  public void setStatusTransaksiMidtrans(String status) {
    this.statusTransaksiMidtrans = status;
  }
  // Midtrans
  public String getMerchantName() {
    return merchantName;
  }
  // Midtrans
  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getUpdateStatusMessage() {
    return updateStatusMessage;
  }

  public void setUpdateStatusMessage(String updateStatusMessage) {
    this.updateStatusMessage = updateStatusMessage;
  }

  public double getHarga() {
    return harga;
  }

  public void setHarga(double harga) {
    this.harga = harga;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getCustomerId() {
    return customer_id;
  }

  public void setCustomerId(String customer_id) {
    this.customer_id = customer_id;
  }

  public String getProductCode() {
    return product_code;
  }

  public void setProductCode(String product_code) {
    this.product_code = product_code;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public int getIconResId() {
    return iconResId;
  }

  public void setIconResId(int iconResId) {
    this.iconResId = iconResId;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getTransactionTime() {
    return transactionTime;
  }

  public void setTransactionTime(String transactionTime) {
    this.transactionTime = transactionTime;
  }

  public long getExpiryDuration() {
    return expiryDuration;
  }

  public void setExpiryDuration(long duration) {
    this.expiryDuration = duration;
  }

  public String getExpiryTime() {
    return expiryTime;
  }

  public void setExpiryTime(String time) {
    this.expiryTime = time;
  }

  protected RiwayatTransaksi(Parcel in) {
    type_api = in.readString();
    ref_id = in.readString();
    tr_id = in.readInt();
    status = in.readString();
    statusTransaksiMidtrans = in.readString();
    message = in.readString();
    updateStatusMessage = in.readString();
    harga = in.readDouble();
    timestamp = in.readLong();
    customer_id = in.readString();
    product_code = in.readString();
    brand = in.readString();
    iconResId = in.readInt();
    imageUrl = in.readString();
    transactionTime = in.readString();
    expiryDuration = in.readLong();
    expiryTime = in.readString();
    merchantName = in.readString();
    callbackUrl = in.readString();
  }

  public static final Creator<RiwayatTransaksi> CREATOR =
      new Creator<RiwayatTransaksi>() {
        @Override
        public RiwayatTransaksi createFromParcel(Parcel in) {
          return new RiwayatTransaksi(in);
        }

        @Override
        public RiwayatTransaksi[] newArray(int size) {
          return new RiwayatTransaksi[size];
        }
      };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(type_api);
    dest.writeString(ref_id);
    dest.writeInt(tr_id);
    dest.writeString(status);
    dest.writeString(statusTransaksiMidtrans);
    dest.writeString(message);
    dest.writeString(updateStatusMessage);
    dest.writeDouble(harga);
    dest.writeLong(timestamp);
    dest.writeString(customer_id);
    dest.writeString(product_code);
    dest.writeString(brand);
    dest.writeInt(iconResId);
    dest.writeString(imageUrl);
    dest.writeString(transactionTime);
    dest.writeLong(expiryDuration);
    dest.writeString(expiryTime);
    dest.writeString(merchantName);
    dest.writeString(callbackUrl);
  }
}

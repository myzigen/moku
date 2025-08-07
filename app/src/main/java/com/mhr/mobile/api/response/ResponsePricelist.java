package com.mhr.mobile.api.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class ResponsePricelist implements Parcelable {

  @SerializedName("brand")
  private String brand;

  @SerializedName("brand_icon_url")
  private String brandIconUrl;

  @SerializedName("product_name")
  private String productName;

  @SerializedName("desc")
  private String desc;

  @SerializedName("buyer_sku_code")
  private String buyerSkuCode;

  @SerializedName("category")
  private String category;

  @SerializedName("type")
  private String type;

  @SerializedName("start_cut_off")
  private String startCutOff;

  @SerializedName("end_cut_off")
  private String endCutOff;

  @SerializedName("seller_name")
  private String sellerName;

  @SerializedName("price")
  private int price;

  @SerializedName("commission")
  private int commission;

  @SerializedName("stock")
  private int stock;

  @SerializedName("buyer_product_status")
  private boolean buyerProductStatus;

  @SerializedName("seller_product_status")
  private boolean sellerProductStatus;

  @SerializedName("jumlah_kuota")
  private String jumlahKuota;

  @SerializedName("masa_aktif")
  private String masaAktif;

  @SerializedName("end_time")
  private String endTime;

  private boolean unlimitedStock;
  private boolean multi;

  @SerializedName("price_merchant")
  private int price_merchant;

  @SerializedName("price_discount")
  private double price_discount;

  @SerializedName("price_after_discount")
  private double price_after_discount;

  @SerializedName("flash_price")
  private int flash_price;

  public ResponsePricelist() {}

  protected ResponsePricelist(Parcel in) {
    brand = in.readString();
    brandIconUrl = in.readString();
    productName = in.readString();
    desc = in.readString();
    jumlahKuota = in.readString();
    masaAktif = in.readString();
    buyerSkuCode = in.readString();
    category = in.readString();
    type = in.readString();
    startCutOff = in.readString();
    endCutOff = in.readString();
    sellerName = in.readString();
    price = in.readInt();
    stock = in.readInt();
    buyerProductStatus = in.readByte() != 0;
    sellerProductStatus = in.readByte() != 0;
    unlimitedStock = in.readByte() != 0;
    multi = in.readByte() != 0;
    price_merchant = in.readInt();
    price_discount = in.readDouble();
    price_after_discount = in.readDouble();
    flash_price = in.readInt();
    commission = in.readInt();
  }

  public static final Creator<ResponsePricelist> CREATOR =
      new Creator<ResponsePricelist>() {
        @Override
        public ResponsePricelist createFromParcel(Parcel in) {
          return new ResponsePricelist(in);
        }

        @Override
        public ResponsePricelist[] newArray(int size) {
          return new ResponsePricelist[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(brand);
    dest.writeString(brandIconUrl);
    dest.writeString(productName);
    dest.writeString(desc);
    dest.writeString(jumlahKuota);
    dest.writeString(masaAktif);
    dest.writeString(buyerSkuCode);
    dest.writeString(category);
    dest.writeString(type);
    dest.writeString(startCutOff);
    dest.writeString(endCutOff);
    dest.writeString(sellerName);
    dest.writeInt(price);
    dest.writeInt(stock);
    dest.writeByte((byte) (buyerProductStatus ? 1 : 0));
    dest.writeByte((byte) (sellerProductStatus ? 1 : 0));
    dest.writeByte((byte) (unlimitedStock ? 1 : 0));
    dest.writeByte((byte) (multi ? 1 : 0));
    dest.writeInt(price_merchant);
    dest.writeDouble(price_discount);
    dest.writeDouble(price_after_discount);
    dest.writeInt(flash_price);
	dest.writeInt(commission);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  // GETTER & SETTER with @PropertyName
  public String getEndTime() {
    return endTime;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getBrandIconUrl() {
    return brandIconUrl;
  }

  public void setBrandIconUrl(String brandIconUrl) {
    this.brandIconUrl = brandIconUrl;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getJumlahKuota() {
    return jumlahKuota;
  }

  public void setJumlahKuota(String jumlahKuota) {
    this.jumlahKuota = jumlahKuota;
  }

  public String getMasaAktif() {
    return masaAktif;
  }

  public String getBuyerSkuCode() {
    return buyerSkuCode;
  }

  public void setBuyerSkuCode(String buyerSkuCode) {
    this.buyerSkuCode = buyerSkuCode;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStartCutOff() {
    return startCutOff;
  }

  public void setStartCutOff(String startCutOff) {
    this.startCutOff = startCutOff;
  }

  public String getEndCutOff() {
    return endCutOff;
  }

  public void setEndCutOff(String endCutOff) {
    this.endCutOff = endCutOff;
  }

  public String getSellerName() {
    return sellerName;
  }

  public void setSellerName(String sellerName) {
    this.sellerName = sellerName;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public boolean getBuyerProductStatus() {
    return buyerProductStatus;
  }

  public void setBuyerProductStatus(boolean buyerProductStatus) {
    this.buyerProductStatus = buyerProductStatus;
  }

  public boolean getSellerProductStatus() {
    return sellerProductStatus;
  }

  public void setSellerProductStatus(boolean sellerProductStatus) {
    this.sellerProductStatus = sellerProductStatus;
  }

  public boolean getUnlimitedStock() {
    return unlimitedStock;
  }

  public void setUnlimitedStock(boolean unlimitedStock) {
    this.unlimitedStock = unlimitedStock;
  }

  public boolean getMulti() {
    return multi;
  }

  public void setMulti(boolean multi) {
    this.multi = multi;
  }

  public int getHargaJual() {
    return price_merchant;
  }

  public void setHargaJual(int hargaJual) {
    this.price_merchant = hargaJual;
  }

  public double getDiskon() {
    return price_discount;
  }

  public void setDiskon(double diskon) {
    this.price_discount = diskon;
  }

  public double getPriceAfterDiskon() {
    return price_after_discount;
  }

  public void setPriceAfterDiskon(double hargaDiskon) {
    this.price_after_discount = hargaDiskon;
  }

  public int getFlashPrice() {
    return flash_price;
  }
  
  public int getCommission(){
	  return commission;
  }
}

package com.mhr.mobile.inquiry.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.PropertyName;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InquiryResponse {
  @SerializedName("data")
  private Data data;

  public Data getData() {
    return data;
  }

  public static class Data implements Parcelable {

    @SerializedName("tr_id")
    private int tr_id;

    @SerializedName("code")
    private String code;

    @SerializedName("hp")
    private String hp;

    @SerializedName("tr_name")
    private String tr_name;

    @SerializedName("period")
    private String period;

    @SerializedName("nominal")
    private double nominal;

    @SerializedName("admin")
    private double admin;

    @SerializedName("ref_id")
    private String ref_id;

    @SerializedName("response_code")
    private String response_code;

    @SerializedName("message")
    private String message;

    @SerializedName("price")
    private double price;

    @SerializedName("selling_price")
    private double selling_price;

    @SerializedName("desc")
    private Desc desc;

    public Data() {}

    // Getters
    public int getTrId() {
      return tr_id;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getHp() {
      return hp;
    }

    public void setHp(String hp) {
      this.hp = hp;
    }

    public String getTrName() {
      return tr_name;
    }

    public void setTrName(String tr) {
      this.tr_name = tr;
    }

    public String getPeriod() {
      return period;
    }

    public double getNominal() {
      return nominal;
    }

    public void setNominal(double nominal) {
      this.nominal = nominal;
    }

    public double getAdmin() {
      return admin;
    }

    public String getRefId() {
      return ref_id;
    }

    public String getResponseCode() {
      return response_code;
    }

    public String getMessage() {
      return message;
    }

    public double getPrice() {
      return price;
    }

    public double getSellingPrice() {
      return selling_price;
    }

    public Desc getDesc() {
      return desc;
    }

    // Parcelable implementation
    protected Data(Parcel in) {
      tr_id = in.readInt();
      code = in.readString();
      hp = in.readString();
      tr_name = in.readString();
      period = in.readString();
      nominal = in.readDouble();
      admin = in.readDouble();
      ref_id = in.readString();
      response_code = in.readString();
      message = in.readString();
      price = in.readDouble();
      selling_price = in.readDouble();
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
      dest.writeInt(tr_id);
      dest.writeString(code);
      dest.writeString(hp);
      dest.writeString(tr_name);
      dest.writeString(period);
      dest.writeDouble(nominal);
      dest.writeDouble(admin);
      dest.writeString(ref_id);
      dest.writeString(response_code);
      dest.writeString(message);
      dest.writeDouble(price);
      dest.writeDouble(selling_price);
      dest.writeParcelable(desc, flags);
    }

    @Override
    public int describeContents() {
      return 0;
    }
  }
  // Simpan Nama Customer Name Ewallet
  public static class EwalletData {
    private String hp;

    @PropertyName("tr_name")
    private String trName;

    @PropertyName("code")
    private String code;

    public EwalletData() {
      // Konstruktor kosong untuk Firebase
    }

    public EwalletData(String hp, String trName, String code) {
      this.hp = hp;
      this.trName = trName;
      this.code = code;
    }

    public String getHp() {
      return hp;
    }

    public String getTrName() {
      return trName;
    }

    public String getCode() {
      return code;
    }

    public void setHp(String hp) {
      this.hp = hp;
    }

    public void setTrName(String trName) {
      this.trName = trName;
    }

    public void setCode(String code) {
      this.code = code;
    }

    // Setter dan getter
  }
  // Simpan Nama Customer Token Pln
  public static class PlnData {

    public PlnData() {
      // untuk firebase
    }
  }

  public static class WifiData {
    private String noWifi;
	private String trName;

    public WifiData() {
      // Firebase
    }

    public WifiData(String noWifi,String trName) {
      this.noWifi = noWifi;
	  this.trName = trName;
    }

    public String getNoWifi() {
      return noWifi;
    }
	
	public String getTrName(){
		return trName;
	}
  }

  public static class Desc implements Parcelable {
    @SerializedName("kode_area")
    private String kodeArea;

    @SerializedName("divre")
    private String divre;

    @SerializedName("datel")
    private String datel;

    @SerializedName("jumlah_tagihan")
    private int jumlahTagihan;

    @SerializedName("tagihan")
    private Tagihan tagihan;

    // Getters
    public String getKodeArea() {
      return kodeArea;
    }

    public String getDivre() {
      return divre;
    }

    public String getDatel() {
      return datel;
    }

    public int getJumlahTagihan() {
      return jumlahTagihan;
    }

    public Tagihan getTagihan() {
      return tagihan;
    }

    // Parcelable implementation
    protected Desc(Parcel in) {
      kodeArea = in.readString();
      divre = in.readString();
      datel = in.readString();
      jumlahTagihan = in.readInt();
      tagihan = in.readParcelable(Tagihan.class.getClassLoader());
    }

    public static final Creator<Desc> CREATOR =
        new Creator<Desc>() {
          @Override
          public Desc createFromParcel(Parcel in) {
            return new Desc(in);
          }

          @Override
          public Desc[] newArray(int size) {
            return new Desc[size];
          }
        };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(kodeArea);
      dest.writeString(divre);
      dest.writeString(datel);
      dest.writeInt(jumlahTagihan);
      dest.writeParcelable(tagihan, flags);
    }

    @Override
    public int describeContents() {
      return 0;
    }
  }

  public static class Tagihan implements Parcelable {
    @SerializedName("detail")
    private List<Detail> detail;

    // Getter
    public List<Detail> getDetail() {
      return detail;
    }

    // Parcelable implementation
    protected Tagihan(Parcel in) {
      detail = in.createTypedArrayList(Detail.CREATOR);
    }

    public static final Creator<Tagihan> CREATOR =
        new Creator<Tagihan>() {
          @Override
          public Tagihan createFromParcel(Parcel in) {
            return new Tagihan(in);
          }

          @Override
          public Tagihan[] newArray(int size) {
            return new Tagihan[size];
          }
        };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeTypedList(detail);
    }

    @Override
    public int describeContents() {
      return 0;
    }
  }

  public static class Detail implements Parcelable {
    @SerializedName("periode")
    private String periode;

    @SerializedName("nilai_tagihan")
    private String nilaiTagihan;

    @SerializedName("admin")
    private String admin;

    @SerializedName("total")
    private int total;

    // Getters
    public String getPeriode() {
      return periode;
    }

    public String getNilaiTagihan() {
      return nilaiTagihan;
    }

    public String getAdmin() {
      return admin;
    }

    public int getTotal() {
      return total;
    }

    // Parcelable implementation
    protected Detail(Parcel in) {
      periode = in.readString();
      nilaiTagihan = in.readString();
      admin = in.readString();
      total = in.readInt();
    }

    public static final Creator<Detail> CREATOR =
        new Creator<Detail>() {
          @Override
          public Detail createFromParcel(Parcel in) {
            return new Detail(in);
          }

          @Override
          public Detail[] newArray(int size) {
            return new Detail[size];
          }
        };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(periode);
      dest.writeString(nilaiTagihan);
      dest.writeString(admin);
      dest.writeInt(total);
    }

    @Override
    public int describeContents() {
      return 0;
    }
  }
}

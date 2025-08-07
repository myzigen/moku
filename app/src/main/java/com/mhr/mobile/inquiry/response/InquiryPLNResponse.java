package com.mhr.mobile.inquiry.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class InquiryPLNResponse implements Parcelable {
  @SerializedName("data")
  private Data data;

  public InquiryPLNResponse() {}

  protected InquiryPLNResponse(Parcel in) {
    data = in.readParcelable(Data.class.getClassLoader());
  }

  public static final Creator<InquiryPLNResponse> CREATOR = new Creator<InquiryPLNResponse>() {
    @Override
    public InquiryPLNResponse createFromParcel(Parcel in) {
      return new InquiryPLNResponse(in);
    }

    @Override
    public InquiryPLNResponse[] newArray(int size) {
      return new InquiryPLNResponse[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(data, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  // Inner class: Data
  public static class Data implements Parcelable {
    @SerializedName("status")
    private String status;

    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("meter_no")
    private String meterNo;

    @SerializedName("subscriber_id")
    private String subscriberId;

    @SerializedName("name")
    private String name;

    @SerializedName("segment_power")
    private String segmentPower;

    @SerializedName("message")
    private String message;

    @SerializedName("rc")
    private String rc;

    public Data() {}

    protected Data(Parcel in) {
      status = in.readString();
      customerId = in.readString();
      meterNo = in.readString();
      subscriberId = in.readString();
      name = in.readString();
      segmentPower = in.readString();
      message = in.readString();
      rc = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
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
      dest.writeString(status);
      dest.writeString(customerId);
      dest.writeString(meterNo);
      dest.writeString(subscriberId);
      dest.writeString(name);
      dest.writeString(segmentPower);
      dest.writeString(message);
      dest.writeString(rc);
    }

    @Override
    public int describeContents() {
      return 0;
    }

    public String getTrName() {
      return name;
    }

    public String getNometer() {
      return meterNo;
    }

    public String getMessage() {
      return message;
    }
	
	public String getSegmentPower(){
		return segmentPower;
	}
  }

  // Inner class: SaveData
  public static class SaveData implements Parcelable {
    private String name;
    private String nometer;
	private String segmenPower;

    public SaveData() {}

    public SaveData(String name, String nometer,String segmentPower) {
      this.name = name;
      this.nometer = nometer;
	  this.segmenPower = segmentPower;
    }

    protected SaveData(Parcel in) {
      name = in.readString();
      nometer = in.readString();
	  segmenPower = in.readString();
    }

    public static final Creator<SaveData> CREATOR = new Creator<SaveData>() {
      @Override
      public SaveData createFromParcel(Parcel in) {
        return new SaveData(in);
      }

      @Override
      public SaveData[] newArray(int size) {
        return new SaveData[size];
      }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(name);
      dest.writeString(nometer);
	  dest.writeString(segmenPower);
    }

    @Override
    public int describeContents() {
      return 0;
    }

    public String getTrName() {
      return name;
    }

    public void setTrName(String name) {
      this.name = name;
    }

    public String getNometer() {
      return nometer;
    }

    public void setNometer(String nometer) {
      this.nometer = nometer;
    }
	
	public String getSegmentPower(){
		return segmenPower;
	}
	
	public void setSegmentPower(String segmentPower){
		this.segmenPower = segmentPower;
	}
  }
}
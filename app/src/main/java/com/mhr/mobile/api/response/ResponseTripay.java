package com.mhr.mobile.api.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResponseTripay {

  @SerializedName("success")
  private boolean success;

  @SerializedName("message")
  private String message;

  @SerializedName("data")
  private List<PaymentMethod> data;

  // Getters and Setters
  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<PaymentMethod> getData() {
    return data;
  }

  public void setData(List<PaymentMethod> data) {
    this.data = data;
  }

  public static class PaymentMethod {

    @SerializedName("group")
    private String group;

    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("fee_merchant")
    private Fee feeMerchant;

    @SerializedName("fee_customer")
    private Fee feeCustomer;

    @SerializedName("total_fee")
    private TotalFee totalFee;

    @SerializedName("minimum_fee")
    private Integer minimumFee;

    @SerializedName("maximum_fee")
    private Integer maximumFee;

    @SerializedName("minimum_amount")
    private int minimumAmount;

    @SerializedName("maximum_amount")
    private int maximumAmount;

    @SerializedName("icon_url")
    private String iconUrl;

    @SerializedName("active")
    private boolean active;

    // Getters and Setters
    public String getGroup() {
      return group;
    }

    public void setGroup(String group) {
      this.group = group;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public Fee getFeeMerchant() {
      return feeMerchant;
    }

    public void setFeeMerchant(Fee feeMerchant) {
      this.feeMerchant = feeMerchant;
    }

    public Fee getFeeCustomer() {
      return feeCustomer;
    }

    public void setFeeCustomer(Fee feeCustomer) {
      this.feeCustomer = feeCustomer;
    }

    public TotalFee getTotalFee() {
      return totalFee;
    }

    public void setTotalFee(TotalFee totalFee) {
      this.totalFee = totalFee;
    }

    public Integer getMinimumFee() {
      return minimumFee;
    }

    public void setMinimumFee(Integer minimumFee) {
      this.minimumFee = minimumFee;
    }

    public Integer getMaximumFee() {
      return maximumFee;
    }

    public void setMaximumFee(Integer maximumFee) {
      this.maximumFee = maximumFee;
    }

    public int getMinimumAmount() {
      return minimumAmount;
    }

    public void setMinimumAmount(int minimumAmount) {
      this.minimumAmount = minimumAmount;
    }

    public int getMaximumAmount() {
      return maximumAmount;
    }

    public void setMaximumAmount(int maximumAmount) {
      this.maximumAmount = maximumAmount;
    }

    public String getIconUrl() {
      return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
      this.iconUrl = iconUrl;
    }

    public boolean isActive() {
      return active;
    }

    public void setActive(boolean active) {
      this.active = active;
    }

    public static class Fee {

      @SerializedName("flat")
      private int flat;

      @SerializedName("percent")
      private double percent;

      // Getters and Setters
      public int getFlat() {
        return flat;
      }

      public void setFlat(int flat) {
        this.flat = flat;
      }

      public double getPercent() {
        return percent;
      }

      public void setPercent(double percent) {
        this.percent = percent;
      }
    }

    public static class TotalFee {

      @SerializedName("flat")
      private int flat;

      @SerializedName("percent")
      private String percent;

      // Getters and Setters
      public int getFlat() {
        return flat;
      }

      public void setFlat(int flat) {
        this.flat = flat;
      }

      public String getPercent() {
        return percent;
      }

      public void setPercent(String percent) {
        this.percent = percent;
      }
    }
  }
}

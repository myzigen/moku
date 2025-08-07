package com.mhr.mobile.api.request.duitku;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DuitkuResponse {
  @SerializedName("success")
  private boolean success;

  @SerializedName("message")
  private String message;

  @SerializedName("status")
  private String status;

  public boolean isSuccess() {
    return success;
  }

  public String getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  @SerializedName("paymentFee")
  private List<PaymentMethod> paymentMethod;

  private String reference;
  private String paymentUrl;
  private String merchantOrderId;
  private String vaNumber;
  private int amount;
  private String statusCode;

  @SerializedName("statusMessage")
  private String statusMessage;

  @SerializedName("ref_id")
  private String refId;

  // ✅ Getter untuk ref_id
  public String getRefId() {
    return refId;
  }

  // ✅ Getter untuk daftar metode pembayaran
  public List<PaymentMethod> getPaymentMethod() {
    return paymentMethod;
  }

  // ✅ Getters untuk response transaksi pembayaran
  public String getReference() {
    return reference;
  }

  public String getPaymentUrl() {
    return paymentUrl;
  }

  public String getMerchantOrderId() {
    return merchantOrderId;
  }

  public String getVaNumber() {
    return vaNumber;
  }

  public int getAmount() {
    return amount;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public String getStatusMessage() {
    return statusMessage;
  }

  // ✅ Inner class: model metode pembayaran
  public static class PaymentMethod {
    public String paymentMethod;
    public String paymentName;
    public String totalFee;
    public String paymentImage;
  }
}

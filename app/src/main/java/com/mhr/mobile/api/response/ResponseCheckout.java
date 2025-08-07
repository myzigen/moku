package com.mhr.mobile.api.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResponseCheckout {

  @SerializedName("success")
  private boolean success;

  @SerializedName("message")
  private String message;

  @SerializedName("data")
  private CheckoutData data;

  public boolean isSuccess() {
    return success;
  }

  public String getMessage() {
    return message;
  }

  public CheckoutData getData() {
    return data;
  }

  public static class CheckoutData {
    @SerializedName("signature")
    private String signature;

    @SerializedName("reference")
    private String reference;

    @SerializedName("merchant_ref")
    private String merchantRef;

    @SerializedName("payment_selection_type")
    private String paymentSelectionType;

    @SerializedName("method")
    private String paymentMethod;

    @SerializedName("payment_name")
    private String paymentName;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("customer_email")
    private String customerEmail;

    @SerializedName("customer_phone")
    private String customerPhone;

    @SerializedName("callback_url")
    private String callbackUrl;

    @SerializedName("return_url")
    private String returnUrl;

    @SerializedName("amount")
    private int amount;

    @SerializedName("fee_merchant")
    private int feeMerchant;

    @SerializedName("fee_customer")
    private int feeCustomer;

    @SerializedName("total_fee")
    private int totalFee;

    @SerializedName("amount_received")
    private int amountReceived;

    @SerializedName("pay_code")
    private String payCode;

    @SerializedName("pay_url")
    private String payUrl;

    @SerializedName("checkout_url")
    private String checkoutUrl;

    @SerializedName("status")
    private String status;

    @SerializedName("expired_time")
    private int expiredTime;

    @SerializedName("order_items")
    public List<OrderItem> orderItems;

    @SerializedName("instructions")
    private List<Instruction> instructions;

    // Getters

    public String getSignature() {
      return signature;
    }

    public void setSignature(String signature) {
      this.signature = signature;
    }

    public String getReference() {
      return reference;
    }

    public String getMerchantRef() {
      return merchantRef;
    }

    public void setMerchantRef(String ref) {
      this.merchantRef = ref;
    }

    public String getPaymentSelectionType() {
      return paymentSelectionType;
    }

    public String getPaymentMethod() {
      return paymentMethod;
    }

    public void setPaymentMethod(String method) {
      this.paymentMethod = method;
    }

    public String getPaymentName() {
      return paymentName;
    }

    public String getCustomerName() {
      return customerName;
    }

    public void setCustomerName(String name) {
      this.customerName = name;
    }

    public String getCustomerEmail() {
      return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
      this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
      return customerPhone;
    }

    public void setCustomerPhone(String phone) {
      this.customerPhone = phone;
    }

    public String getCallbackUrl() {
      return callbackUrl;
    }

    public String getReturnUrl() {
      return returnUrl;
    }

    public int getAmount() {
      return amount;
    }

    public void setAmount(int amount) {
      this.amount = amount;
    }

    public int getFeeMerchant() {
      return feeMerchant;
    }

    public int getFeeCustomer() {
      return feeCustomer;
    }

    public int getTotalFee() {
      return totalFee;
    }

    public int getAmountReceived() {
      return amountReceived;
    }

    public String getPayCode() {
      return payCode;
    }

    public String getPayUrl() {
      return payUrl;
    }

    public String getCheckoutUrl() {
      return checkoutUrl;
    }

    public String getStatus() {
      return status;
    }

    public int getExpiredTime() {
      return expiredTime;
    }
	
	public void setExpiredTime(int expired){
		this.expiredTime = expired;
	}

    public List<OrderItem> getOrderItems() {
      return orderItems;
    }

    public List<Instruction> getInstructions() {
      return instructions;
    }
  }

  public static class OrderItem {

    @SerializedName("sku")
    private String sku;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private int price;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("subtotal")
    private int subtotal;

    @SerializedName("product_url")
    private String productUrl;

    @SerializedName("image_url")
    private String imageUrl;

    // Getters

    public String getSku() {
      return sku;
    }

    public void setSku(String sku) {
      this.sku = sku;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getPrice() {
      return price;
    }

    public void setPrice(int price) {
      this.price = price;
    }

    public int getQuantity() {
      return quantity;
    }

    public void setQuantity(int qty) {
      this.quantity = qty;
    }

    public int getSubtotal() {
      return subtotal;
    }

    public String getProductUrl() {
      return productUrl;
    }

    public String getImageUrl() {
      return imageUrl;
    }
  }

  public static class Instruction {

    @SerializedName("title")
    private String title;

    @SerializedName("steps")
    private List<String> steps;

    // Getters

    public String getTitle() {
      return title;
    }

    public List<String> getSteps() {
      return steps;
    }
  }
}

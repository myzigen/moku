package com.mhr.mobile.api.request;

import android.app.Activity;
import com.google.gson.Gson;
import com.mhr.mobile.api.client.ClientTripay;
import com.mhr.mobile.api.response.ResponseCheckout;
import com.mhr.mobile.api.response.ResponseTripay;
import com.mhr.mobile.util.SignMaker;
import java.util.ArrayList;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RequestTripay {
  private String merchantCode = "T36161";
  private String privateKey = "hLeDv-uymOI-caiIP-Bg5UT-MImym";
  public final Activity activity;
  private RequestBody rb;
  private String header;
  private String payment_method;
  private String customer_name;
  private String customer_phone;
  private String customer_email;
  private String merchant_ref;
  private int amount;

  // OrderItems
  private String name;
  private int price;
  private int quantity;
  private String sku;

  public RequestTripay(Activity activity) {
    this.activity = activity;
  }

  public RequestTripay Header(String header) {
    this.header = header;
    return this;
  }

  public RequestTripay PaymentMethod(String paymentMethod) {
    this.payment_method = paymentMethod;
    return this;
  }

  public RequestTripay CustomerName(String name) {
    this.customer_name = name;
    return this;
  }

  public RequestTripay CustomerPhone(String phone) {
    this.customer_phone = phone;
    return this;
  }

  public RequestTripay CustomerEmail(String email) {
    this.customer_email = email;
    return this;
  }

  public RequestTripay MerchantRef(String ref) {
    this.merchant_ref = ref;
    return this;
  }

  public RequestTripay Amount(int amount) {
    this.amount = amount;
    return this;
  }

  public RequestTripay ProdukName(String name) {
    this.name = name;
    return this;
  }

  public RequestTripay ProdukPrice(int price) {
    this.price = price;
    return this;
  }

  public RequestTripay Quantity(int qty) {
    this.quantity = qty;
    return this;
  }

  public RequestTripay Sku(String sku) {
    this.sku = sku;
    return this;
  }

  public void requestPaymentChannel(CallbackChannel channel) {
    ClientTripay<ResponseTripay> requestDataOtp = new ClientTripay<>(activity);
    Call<ResponseTripay> call = requestDataOtp.getEndpoint().getPaymentTripay(header);

    requestDataOtp.executeSingle(
        call,
        new ClientTripay.CallbackSingle<ResponseTripay>() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(ResponseTripay data) {
            channel.onDataChange(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            channel.onFailure(errorMessage);
          }
        });
  }

  public void requestPaymentCheckout(CallbackCheckout channel) {
    ResponseCheckout.CheckoutData body = new ResponseCheckout.CheckoutData();
    body.setPaymentMethod(payment_method);
    body.setCustomerName(customer_name);
    body.setCustomerPhone(customer_phone);
    body.setCustomerEmail(customer_email);
    body.setMerchantRef(merchant_ref);
    body.setAmount(amount);
    int expiredTimestamp =
        (int) (System.currentTimeMillis() / 1000L + (60 * 60)); // 1 jam dari sekarang
    body.setExpiredTime(expiredTimestamp);

    // String signature = SignMaker.SignatureTripay(privateKey, merchantCode, merchant_ref, amount);
    String signature = SignMaker.SignatureTripay(merchantCode, merchant_ref, amount, privateKey);
    body.setSignature(signature);
    ResponseCheckout.OrderItem item = new ResponseCheckout.OrderItem();
    item.setName(name);
    item.setPrice(price);
    item.setQuantity(quantity);
    item.setSku(sku);

    body.orderItems = new ArrayList<>();
    body.orderItems.add(item);

    String jsonBody = new Gson().toJson(body);
    rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    ClientTripay<ResponseCheckout> requestDataOtp = new ClientTripay<>(activity);
    Call<ResponseCheckout> call = requestDataOtp.getEndpoint().getPaymentCheckout(header, rb);

    requestDataOtp.executeSingle(
        call,
        new ClientTripay.CallbackSingle<ResponseCheckout>() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(ResponseCheckout data) {
            channel.onPayment(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            channel.onFailure(errorMessage);
          }
        });
  }

  public interface CallbackChannel {
    void onRequest();

    void onDataChange(ResponseTripay tripay);

    void onFailure(String errorMessage);
  }

  public interface CallbackCheckout {
    void onRequest();

    void onPayment(ResponseCheckout tripay);

    void onFailure(String errorMessage);
  }
}

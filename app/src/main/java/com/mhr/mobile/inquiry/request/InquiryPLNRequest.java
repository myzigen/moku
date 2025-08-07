package com.mhr.mobile.inquiry.request;

import android.app.Activity;
import com.mhr.mobile.inquiry.client.InquiryPlnClient;
import com.mhr.mobile.inquiry.response.InquiryPLNResponse;
import com.mhr.mobile.util.Config;
import com.mhr.mobile.util.SignMaker;

public class InquiryPLNRequest {
  public Activity activity;
  private String username;
  private String customer_id;
  private String sign;
  private String apikey;

  public InquiryPLNRequest(Activity activity) {
    this.activity = activity;
  }

  public InquiryPLNRequest(String username, String customerId, String sign) {
    this.username = username;
    this.customer_id = customerId;
    this.sign = sign;
  }

  public void setUsername() {
	  this.username = Config.USERNAME;
  }

  public void setApikey() {
	  this.apikey = Config.API_KEY_PRODUCTION;
  }

  public void setNometer(String nometer) {
    this.customer_id = nometer;
  }

  public void startInquiryPln(InquiryCallback callback) {
    sign = SignMaker.getSign(username, apikey, customer_id);
	InquiryPlnClient.getInstance().execute(this, username, customer_id, sign, apikey, callback);
  }

  public interface InquiryCallback {
    void onStartLoading();

    void onResponse(InquiryPLNResponse response);

    void onFailure(String errorMessage);
  }
}

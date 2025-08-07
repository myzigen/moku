package com.mhr.mobile.inquiry;

import android.app.Activity;
import com.mhr.mobile.inquiry.helper.EwalletHelper;
import com.mhr.mobile.inquiry.helper.PlnHelper;
import com.mhr.mobile.inquiry.response.InquiryPLNResponse.SaveData;
import com.mhr.mobile.inquiry.response.InquiryResponse;

public class InquiryCustomer {
  private Activity activity;
  private EwalletHelper ewallet;
  private String nometer;

  public InquiryCustomer(Activity activity) {
    this.activity = activity;
  }

  public static InquiryCustomer with(Activity context) {
    return new InquiryCustomer(context);
  }

  public InquiryCustomer InquiryPLNToken(String nometer, Callback i) {
    this.nometer = nometer;
    PlnHelper plnHelper = new PlnHelper(activity);
    plnHelper.checkCustomerName(
        nometer,
        new PlnHelper.PlnCallback() {
          @Override
          public void onLoading() {
            i.onLoading();
          }

          @Override
          public void onDataFound(SaveData data) {
            i.onSuccess(data.getTrName());
          }

          @Override
          public void onDataSaved() {}

          @Override
          public void onError(String errorMessage) {
            i.onError(errorMessage);
          }
        });
    return this;
  }
  

  public InquiryCustomer InquiryEwallet(String nomor,String brand, Callback c) {
    ewallet = new EwalletHelper(activity);
    ewallet.checkCustomerName(
        nomor,
        brand,
        20000,
        new EwalletHelper.EwalletCallback() {
          @Override
          public void onLoading() {
			  c.onLoading();
		  }

          @Override
          public void onDataFound(InquiryResponse.EwalletData data) {
			  c.onSuccess(data.getTrName());
		  }

          @Override
          public void onDataSaved() {
            // Handle jika data berhasil disimpan
            // isNomorChecked = false;
          }

          @Override
          public void onError(String errorMessage) {
            // isNomorChecked = false;
			c.onError(errorMessage);
          }
        });

    return this;
  }

  public interface Callback {
    public void onLoading();

    public void onSuccess(String trName);

    public void onError(String error);
  }
}

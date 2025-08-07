package com.mhr.mobile.api.request;

import android.app.Activity;
import com.google.gson.Gson;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.response.ResponseInquiryPasca;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RequestInquiryPasca {
  public Activity activity;
  private RequestBody rb;
  private String token;
  private String sku;
  private String customer_no;
  private boolean testing;

  public RequestInquiryPasca(Activity activity) {
    this.activity = activity;
  }
  
  public RequestInquiryPasca(String token,String sku,String customer_no,boolean testing){
	  this.token = token;
	  this.sku = sku;
	  this.customer_no = customer_no;
	  this.testing = testing;
  }
  
  public void setToken(String token){
	  this.token = token;
  }
  public void setSku(String sku){
	  this.sku = sku;
  }
  
  public void setCustomerNo(String customer_no){
	  this.customer_no = customer_no;
  }
  
  public void setTesting(boolean testing){
	  this.testing = testing;
  }

  public void requestInquiryPasca(Callback callback) {
    String jsonBody = new Gson().toJson(new RequestInquiryPasca(token, sku, customer_no, testing));
    rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    ClientApi<ResponseInquiryPasca> clientApi = new ClientApi<>(activity);
    Call<ResponseInquiryPasca> call = clientApi.getEndpoint().getInquiryPasca(rb);

    clientApi.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseInquiryPasca>() {
          @Override
          public void onRequest() {
            callback.onRequest();
          }

          @Override
          public void onDataChange(ResponseInquiryPasca data) {
            callback.onDataChanged(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public interface Callback {
    void onRequest();

    void onDataChanged(ResponseInquiryPasca inquiry);

    void onFailure(String error);
  }
}

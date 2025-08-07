package com.mhr.mobile.manage.request;

import android.app.Activity;
import com.mhr.mobile.manage.client.BukaolshopClient;
import com.mhr.mobile.manage.response.MarketplaceResponse;
import com.mhr.mobile.manage.response.SliderHomeResponse;
import java.util.List;

public class MarketplaceRequest {
  public Activity activity;
  private String token;

  public MarketplaceRequest(Activity activity) {
    this.activity = activity;
  }
  
  public void setApiKey(){
	  this.token = "eyJhcHAiOiIxODM1IiwiYXV0aCI6IjIwMjAwMjIwIiwic2lnbiI6InZvZEtMRnBWcmV6TFwvbk1yTmY1M1lRPT0ifQ==";
  }

  public void startRequestSlide(RequestSlide callback) {
    BukaolshopClient.getInstance().execute(this, token, callback);
  }

  public void startRequestProduk(RequestProduk callback) {
    BukaolshopClient.getInstance().execute(this, token, callback);
  }

  public interface RequestProduk {
    void onStartRequest();

    void onResponse(List<MarketplaceResponse.Data> data);

    void onFailure(String error);
  }

  public interface RequestSlide {
    void onStartRequest();

    void onResponse(SliderHomeResponse response);

    void onFailure(String error);
  }
}

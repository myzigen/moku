package com.mhr.mobile.inquiry.request;

import android.app.Activity;
import com.mhr.mobile.inquiry.client.InquiryClient;
import com.mhr.mobile.inquiry.response.InquiryResponse;
import com.mhr.mobile.util.Config;
import com.mhr.mobile.util.SignMaker;

public class InquiryRequest {
  public Activity activity;
  private String commands;
  private String username;
  private String code;
  private String hp;
  private String ref_id;
  private String sign;
  private String apiKey;
  private int amount; // Tambahkan field amount untuk nominal custom
  private Desc desc;

  public InquiryRequest(Activity activity) {
    this.activity = activity;
  }

  public InquiryRequest(
      String commands,
      String username,
      String code,
      String hp,
      String ref_id,
      String sign,
      int amount) {
    this.commands = commands;
    this.username = username;
    this.code = code;
    this.hp = hp;
    this.ref_id = ref_id;
    this.sign = sign;
    this.amount = amount;
    this.desc = new Desc(amount); // Menambahkan objek desc
  }

  public void setCommands(String commands) {
    this.commands = commands;
  }

  public void setUsername() {
    this.username = Config.USERNAME;
  }

  public void setApiKey() {
    this.apiKey = Config.API_KEY_PRODUCTION;
  }

  public void setCodeProduk(String code) {
    this.code = code;
  }

  public void setHp(String hp) {
    this.hp = hp;
  }

  public void setRefId(String ref_id) {
    this.ref_id = ref_id;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public void setAmount(int amount) {
    this.amount = amount;
    this.desc = new Desc(amount); // Set amount di desc
  }

  public void startInquiryRequest(InquiryCallback callback) {
    ref_id = SignMaker.getSignRefId();
    sign = SignMaker.getSign(username, apiKey, ref_id);
    InquiryClient.getInstance().execute(this, "inq-pasca", username, code, hp, ref_id, sign, apiKey, amount, callback);
  }

  public interface InquiryCallback {
    void onStartLoading();

    void onResponse(InquiryResponse response);

    void onFailure(String errorMessage);
  }

  // Class untuk desc (nominal amount)
  public class Desc {
    private int amount;

    public Desc(int amount) {
      this.amount = amount;
    }
  }
}

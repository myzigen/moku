package com.mhr.mobile.api.request;

import android.app.Activity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.listener.CheckNomorListener;
import com.mhr.mobile.api.listener.UsersDataListener;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.ui.intro.UserSession;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.SignMaker;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;

public class RequestUsers {
  private Activity activity;
  private RequestBody rb;
  private UserSession pref;
  private String token, nomor, nama, verifyOtp, sign, pin,newPin;
  private long kadaluwarsa;
  private String field;
  private boolean value;

  public RequestUsers(Activity activity) {
    this.activity = activity;
    this.pref = UserSession.with(activity);
  }

  // Data Users
  public RequestUsers(String token, String nomor, String sign) {
    this.token = token;
    this.nomor = nomor;
    this.sign = sign;
  }

  public static RequestUsers with(Activity activity) {
    return new RequestUsers(activity);
  }

  public RequestUsers Token() {
    this.token = pref.getToken();
    return this;
  }

  public RequestUsers Nama() {
    this.nama = pref.getNama();
    return this;
  }

  public RequestUsers Nama(String nama) {
    this.nama = nama;
    return this;
  }

  public RequestUsers Nomor() {
    this.nomor = pref.getNomor();
    return this;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setNomor(String nomor) {
    this.nomor = nomor;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }
  
  public void setNewPin(String newPin){
	  this.newPin = newPin;
  }

  public void setField(String field) {
    this.field = field;
  }

  public void setValue(boolean value) {
    this.value = value;
  }

  public void ValidatePin(CheckNomorListener callback) {
    this.nomor = FormatUtils.normalizeNomor(nomor);
    JsonObject json = new JsonObject();
    json.addProperty("token", token);
    json.addProperty("nomor", nomor);
    json.addProperty("pin", pin);
	json.addProperty("new_pin",newPin);
    if (field != null) {
      json.addProperty("field", field);
      json.addProperty("value", value);
    }

    rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
    ClientApi<ResponseUsers> requestDataOtp = new ClientApi<>(activity);
    Call<ResponseUsers> call = requestDataOtp.getEndpoint().verifyPin(rb);

    requestDataOtp.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseUsers>() {
          @Override
          public void onRequest() {
            callback.onRequest();
          }

          @Override
          public void onDataChange(ResponseUsers data) {
            callback.onCheckNomor(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public void CheckNomorTerdaftar(CheckNomorListener callback) {
    this.nomor = FormatUtils.normalizeNomor(nomor);
    ClientApi<ResponseUsers> requestDataOtp = new ClientApi<>(activity);
    Call<ResponseUsers> call = requestDataOtp.getEndpoint().checkNomorTerdaftar(nomor);

    requestDataOtp.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseUsers>() {
          @Override
          public void onRequest() {
            callback.onRequest();
          }

          @Override
          public void onDataChange(ResponseUsers data) {
            callback.onCheckNomor(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailure(errorMessage);
          }
        });
  }

  public void GetDataUsers(UsersDataListener listener) {
    this.nomor = FormatUtils.normalizeNomor(this.nomor);
    this.sign = SignMaker.md5(token + nomor);
    String json = new Gson().toJson(new RequestUsers(token, nomor, sign));
    rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    ClientApi<ResponseUsers> serviceOtp = new ClientApi<>(activity);
    Call<ResponseUsers> call = serviceOtp.getEndpoint().getUsers(rb);

    serviceOtp.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseUsers>() {
          @Override
          public void onRequest() {
            listener.onRequest();
          }

          @Override
          public void onDataChange(ResponseUsers data) {
            listener.onReceive(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            listener.onFailure(errorMessage);
          }
        });
  }

  public void requestDeleteAccount(CallbackDelete callback) {
    JSONObject json = new JSONObject();
    try {
      json.put("pin", pin);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    rb = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

    ClientApi<ResponseBody> client = new ClientApi<>(activity);
    Call<ResponseBody> call = client.getEndpoint().deleteAkun(token, rb);

    client.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseBody>() {
          @Override
          public void onRequest() {
			  callback.onRequest();
		  }

          @Override
          public void onDataChange(ResponseBody data) {
            try {
              String jsonStr = data.string();
              JSONObject res = new JSONObject(jsonStr);
              String status = res.optString("status");
              String message = res.optString("message");

              if ("success".equalsIgnoreCase(status)) {
                callback.onSuccess(message);
              } else {
                callback.onFailed(message);
              }
            } catch (Exception e) {
              callback.onFailed("Gagal parsing respon");
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onFailed(errorMessage);
          }
        });
  }

  public interface CallbackDelete {
    void onRequest();

    void onSuccess(String message);

    void onFailed(String error);
  }
}

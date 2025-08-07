package com.mhr.mobile.api.request;

import android.app.Activity;
import com.mhr.mobile.api.client.ClientApi;
import com.mhr.mobile.api.listener.OtpRequestListener;
import com.mhr.mobile.api.listener.OtpVerifyListener;
import com.mhr.mobile.api.response.ResponseOtp;
import com.mhr.mobile.util.FormatUtils;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Call;

public class RequestOtp {
  private Activity activity;
  private String nomor, verifyOtp;
  private long kadaluwarsa;

  public RequestOtp(Activity activity) {
    this.activity = activity;
  }

  public static RequestOtp with(Activity activity) {
    return new RequestOtp(activity);
  }

  public RequestOtp Nomor(String nomor) {
    this.nomor = FormatUtils.normalizeNomor(nomor);
    return this;
  }

  public RequestOtp VerifiyOtp(String verifyOtp) {
    this.verifyOtp = verifyOtp;
    return this;
  }

  public long getWaktu() {
    return kadaluwarsa;
  }

  public void RequestOtp(OtpRequestListener listener) {
    ClientApi<ResponseBody> requestOtp = new ClientApi<>(activity);
    Call<ResponseBody> call = requestOtp.getEndpoint().endpointOtp(nomor);
    requestOtp.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseBody>() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(ResponseBody data) {
            try {
              String responseString = data.string();
              JSONObject jsonObject = new JSONObject(responseString);

              // ambil kadaluwarsa dari JSON jika ada
              if (jsonObject.has("kadaluwarsa")) {
                kadaluwarsa = jsonObject.getLong("kadaluwarsa");
              }

              listener.onReceive();
            } catch (Exception e) {
              listener.onError("Gagal parsing response");
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            listener.onError(errorMessage);
          }
        });
  }

  public void RequestVerifyOtp(OtpVerifyListener listener) {
    ClientApi<ResponseOtp> serviceOtp = new ClientApi<>(activity);
    Call<ResponseOtp> call = serviceOtp.getEndpoint().endpointVerifyOtp(nomor, verifyOtp);

    serviceOtp.executeSingle(
        call,
        new ClientApi.CallbackSingle<ResponseOtp>() {
          @Override
          public void onRequest() {
            listener.onRequest();
          }

          @Override
          public void onDataChange(ResponseOtp data) {
            listener.onReceive(data);
          }

          @Override
          public void onFailure(String errorMessage) {
            listener.onFailure(errorMessage);
          }
        });
  }
}

package com.mhr.mobile.api.request;

import android.app.Activity;
import android.widget.Toast;

import com.google.android.play.core.integrity.IntegrityManager;
import com.google.android.play.core.integrity.IntegrityManagerFactory;
import com.google.android.play.core.integrity.IntegrityTokenRequest;
import java.io.IOException;
import java.util.UUID;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestIntegrityChecker {
  private final Activity activity;

  public RequestIntegrityChecker(Activity activity) {
    this.activity = activity;
  }

  public void StartIntegrity(OnCheckComplete listener) {
    IntegrityManager manager = IntegrityManagerFactory.create(activity); 
    String nonce = UUID.randomUUID().toString();
    IntegrityTokenRequest tokenRequest = IntegrityTokenRequest.builder().setNonce(nonce).build();
    manager
        .requestIntegrityToken(tokenRequest)
        .addOnSuccessListener(
            response -> {
              String integrityToken = response.token();
              listener.onTokenReceived(integrityToken);
            })
        .addOnFailureListener(
            e -> {
              listener.onError(e.getMessage());
            });
  }

  public void sendTokenToServer(String token, OnCheckServe serve) {
    OkHttpClient client = new OkHttpClient();

    JSONObject json = new JSONObject();
    try {
      json.put("integrity_token", token);
    } catch (JSONException e) {
      e.printStackTrace();
      return;
    }

    RequestBody body = RequestBody.create(MediaType.get("application/json"), json.toString());

    Request request =
        new Request.Builder()
            .url("https://api.qiospro.my.id/api/users/users-verify-integrity.php")
            .post(body)
            .build();

    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(
                    () -> {
                      Toast.makeText(activity, "Gagal koneksi ke server", Toast.LENGTH_SHORT)
                          .show();
                      // finishAffinity(); // Blokir app
                      serve.onCheck();
                    });
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                  String responseBody = response.body().string();

                  try {
                    JSONObject res = new JSONObject(responseBody);
                    String status = res.optString("status");

                    if ("fraud".equals(status)) {
                      // ❌ Deteksi kecurangan
                      activity.runOnUiThread(
                          () -> {
                            Toast.makeText(activity, "Akses tidak sah", Toast.LENGTH_SHORT).show();
                            // finishAffinity(); // keluar app
                            serve.onCheck();
                          });
                    }

                  } catch (JSONException e) {
                    e.printStackTrace();
                  }
                }
              }
            });
  }

  public interface OnCheckServe {
    void onCheck();
  }

  public interface OnCheckComplete {
    void onTokenReceived(String token);

    void onError(String error);
  }
}

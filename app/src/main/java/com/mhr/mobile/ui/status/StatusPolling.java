package com.mhr.mobile.ui.status;

import android.app.Activity;
import android.content.Context;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class StatusPolling {
  private Activity context;
  private String refId;

  public StatusPolling(Activity context) {
    this.context = context;
  }

  public static StatusPolling with(Activity context) {
    return new StatusPolling(context);
  }

  public StatusPolling refId(String refId) {
    this.refId = refId;
    return this;
  }

  public void checkStatus(TopupStatusCallback callback) {
    OkHttpClient client = new OkHttpClient();
    String url = "https://api.qiospro.my.id/api/status_topup.php?ref_id=" + refId;

    Request request = new Request.Builder().url(url).get().build();

    client
        .newCall(request)
        .enqueue(
            new okhttp3.Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                // Bisa log error jika mau
                context.runOnUiThread(() -> callback.onFailed(e.getMessage()));
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                  try {
                    JSONObject json = new JSONObject(response.body().string());
                    if (json.optBoolean("status")) {
                      JSONObject data = json.optJSONObject("data");
                      String newStatus = data.optString("status");
					  context.runOnUiThread(() -> callback.onSuccess(refId,newStatus));
             
                    } else {
                      context.runOnUiThread(() -> callback.onFailed("Status false dari server"));
                    }
                  } catch (JSONException e) {
                    context.runOnUiThread(() -> callback.onFailed(e.getMessage()));
                  }
                } else {
                  context.runOnUiThread(() -> callback.onFailed("Respon tidak sukses"));
                }
              }
            });
  }

  public interface TopupStatusCallback {
    void onSuccess(String refId, String newStatus);

    void onFailed(String error);
  }
}

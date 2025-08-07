package com.mhr.mobile.inquiry.request;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import java.io.IOException;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

public class InquiryGames {

  public interface GameCallback {
	void onStart();
	
    void onSuccess(String nickname);

    void onError(String message);
  }

  private static final String API_URL = "https://api.qiospro.my.id/api/inquiry/inquiry-games.php";
  private static final String SAVE_URL = "https://api.qiospro.my.id/api/inquiry/save_inquiry_games.php";
  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  public static void request(Activity activity, String game, String userId, String zoneId, GameCallback callback) {
    OkHttpClient client = new OkHttpClient();

    JSONObject json = new JSONObject();
    try {
      json.put("type_name", game);
      json.put("userId", userId);
      json.put("zoneId", zoneId != null ? zoneId : "");
    } catch (JSONException e) {
      callback.onError("Gagal membuat data JSON");
      return;
    }

    RequestBody body = RequestBody.create(json.toString(), JSON);

    Request request =
        new Request.Builder()
            .url(API_URL)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build();
			
	callback.onStart();
	
    client
        .newCall(request)
        .enqueue(
            new Callback() {
              final Handler handler = new Handler(Looper.getMainLooper());

              @Override
              public void onFailure(Call call, IOException e) {
                handler.post(() -> callback.onError("Gagal koneksi: " + e.getMessage()));
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                  handler.post(() -> callback.onError("Error: " + response.code()));
                  return;
                }

                String responseData = response.body().string();

                handler.post(
                    () -> {
                      try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        if (jsonResponse.getBoolean("status")) {
                          JSONObject data = jsonResponse.getJSONObject("data");
                          String nickname = data.getString("nickname");

                          // 🔁 Kirim ke database kamu
                          kirimKeDatabaseSendiri(game, userId, zoneId, nickname);

                          callback.onSuccess(nickname);
                        } else {
                          callback.onError("ID tidak ditemukan atau salah");
                        }
                      } catch (JSONException e) {
                        callback.onError("Format response tidak valid");
                      }
                    });
              }
            });
  }

  private static void kirimKeDatabaseSendiri(String gameCode, String idGame, String zoneId, String nickname) {
    OkHttpClient client = new OkHttpClient();

    JSONObject json = new JSONObject();
    try {
      json.put("game_code", gameCode.toLowerCase());
      json.put("id_games", idGame);
      json.put("zone_id", zoneId);
      json.put("nickname", nickname);
      json.put("status", true);
      json.put("message", "Saved from client");
    } catch (JSONException e) {
      e.printStackTrace();
      return;
    }

    RequestBody body = RequestBody.create(json.toString(), JSON);

    Request request =
        new Request.Builder()
            .url(SAVE_URL)
            .post(body)
            .build();

    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                // opsional log kesalahan
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                // opsional log respons
              }
            });
  }
}
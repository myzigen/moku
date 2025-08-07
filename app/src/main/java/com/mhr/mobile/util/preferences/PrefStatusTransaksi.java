package com.mhr.mobile.util.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.json.JSONException;
import org.json.JSONObject;

public class PrefStatusTransaksi {
  public static final String PREF_NAME = "pref_status_transaksi";
  private SharedPreferences preferences;
  private SharedPreferences.Editor editor;

  public PrefStatusTransaksi(Context context) {
    try {
      MasterKey masterKey =
          new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();

      preferences =
          EncryptedSharedPreferences.create(
              context,
              PREF_NAME,
              masterKey,
              EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
              EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

      editor = preferences.edit();
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException("Gagal menginisialisasi EncryptedSharedPreferences", e);
    }
  }

  public static PrefStatusTransaksi getInstance(Context ctx) {
    return new PrefStatusTransaksi(ctx);
  }

  public void saveStatus(JSONObject data) {
    editor.putString(data.optString("ref_id"), data.toString()).apply();
  }

  public JSONObject getStatus(String refId) {
    String json = preferences.getString(refId, null);
    try {
      return json != null ? new JSONObject(json) : null;
    } catch (JSONException e) {
      return null;
    }
  }

  public void setNotificationShown(String refId) {
    editor.putBoolean("notif_shown_" + refId, true).apply();
  }

  public boolean isNotificationShown(String refId) {
    return preferences.getBoolean("notif_shown_" + refId, false);
  }
}

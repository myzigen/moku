package com.mhr.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import com.mhr.mobile.api.response.ResponseUsers;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class QiosPreferences {
  public static final String PREF_NAME = "secure_pref";
  public static final String SAVE_NOMOR = "nomor";
  public static final String SAVE_INQUIRY = "nometer";
  public static final String IS_HIDDEN_SALDO = "hidden";
  private static final String KEY_SALDO = "saldo";
  private static final String COUNTDOWN = "expired";

  private SharedPreferences preferences;
  private SharedPreferences.Editor editor;

  public QiosPreferences(Context context) {
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

  // ===== Generic =====
  public void setString(String key, String value) {
    editor.putString(key, value).apply();
  }

  public String getString(String key) {
    return preferences.getString(key, "");
  }

  public void setBoolean(String key, boolean value) {
    editor.putBoolean(key, value).apply();
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    return preferences.getBoolean(key, defaultValue);
  }

  public void remove(String key) {
    editor.remove(key).apply();
  }

  // ===== Login Session =====
  public void saveLogin(boolean login) {
    setBoolean("session_login", login);
  }

  public boolean isLogin() {
    return getBoolean("session_login", false);
  }

  public void clearSessionLogin() {
    editor.clear().apply();
  }

  public void clearOnlySession() {
    editor.remove("otp_verified");
    // Jangan hapus nomor_user kalau ingin tetap disimpan
    editor.apply();
  }

  public void setOtpVerified(boolean verified) {
    editor.putBoolean("otp_verified", verified).apply();
  }

  public boolean isOtpVerified() {
    return preferences.getBoolean("otp_verified", false);
  }

  // ===== Session User Object =====
  public void saveUserSession(ResponseUsers.User user) {
    editor.putString("session_token", user.getToken()).apply();
    editor.putString("session_nama", user.getNama()).apply();
    editor.putString("session_nomor", user.getNomor()).apply();
    editor.putString("session_nama_toko", user.getNamaToko()).apply();
	editor.putString("session_alamat_toko", user.getAlamatToko()).apply();
    editor.putBoolean("session_pin", user.getHashPin()).apply();
    editor.putBoolean("session_login", true).apply(); // Tambahkan ini
  }

  public ResponseUsers.User getUserSession() {
    return new ResponseUsers.User(
        preferences.getString("session_token", ""),
        preferences.getString("session_nama", ""),
        preferences.getString("session_nomor", ""),
        preferences.getString("session_nama_toko", ""),
		preferences.getString("session_alamat_toko",""),
        preferences.getBoolean("session_pin", false));
  }

  // ===== Data Tambahan Non-session =====
  public void saveTotalDownline(int total) {
    editor.putInt("total_downline", total).apply();
  }

  public int getTotalDownline() {
    return preferences.getInt("total_downline", 0);
  }

  public void setPin(boolean pin) {
    editor.putBoolean("user_pin", pin).apply();
  }

  public boolean getPin() {
    return preferences.getBoolean("user_pin", true);
  }

  public void setPinPurchase(boolean pin) {
    editor.putBoolean("pin_purchase", pin).apply();
  }

  public boolean getPinPurchase() {
    return preferences.getBoolean("pin_purchase", true);
  }

  public void saveNomerTerakhirInput(String nomer) {
    editor.putString(SAVE_NOMOR, nomer).apply();
  }

  public String getNomerTerakhirInput() {
    return preferences.getString(SAVE_NOMOR, "");
  }

  public void hapusNomerTerakhirDisimpan() {
    editor.remove(SAVE_NOMOR).apply();
  }

  public void saveIdMeterTerakhirInput(String noMeter) {
    editor.putString(SAVE_INQUIRY, noMeter).apply();
  }

  public String getNomerMeterTerakhirInput() {
    return preferences.getString(SAVE_INQUIRY, "");
  }

  public void setHiddenSaldo(boolean isHidden) {
    editor.putBoolean(IS_HIDDEN_SALDO, isHidden).apply();
  }

  public boolean isHiddenSaldo() {
    return preferences.getBoolean(IS_HIDDEN_SALDO, false);
  }

  public void setSaldo(String saldo) {
    editor.putString(KEY_SALDO, saldo).apply();
  }

  public String getSaldo() {
    return preferences.getString(KEY_SALDO, "0");
  }

  // ===== Waktu Countdown Timer =====
  public void saveCountdownTimer(long durationInMinutes, String orderId) {
    long expired = System.currentTimeMillis() + (durationInMinutes * 60 * 1000);
    editor.putLong(COUNTDOWN + "_" + orderId, expired).apply();
  }

  public long getCountdownTimer(String orderId) {
    long expiryTime = preferences.getLong(COUNTDOWN + "_" + orderId, 0);
    long currentTime = System.currentTimeMillis();
    long timeLeft = expiryTime - currentTime;
    return Math.max(timeLeft, 0);
  }

  // ===== Cut Off System =====
  public void startCutOff(String startCutOff) {
    editor.putString("start_cut_off", startCutOff).apply();
  }

  public String getStartCutOff() {
    return preferences.getString("start_cut_off", "");
  }

  public void endCutOff(String endCutOff) {
    editor.putString("end_cut_off", endCutOff).apply();
  }

  public String getEndCutOff() {
    return preferences.getString("end_cut_off", "");
  }

  public void setPinLockedUntil(String nomor, long timestamp) {
    editor.putLong("pin_locked_until_" + nomor, timestamp).apply();
  }

  public long getPinLockedUntil(String nomor) {
    return preferences.getLong("pin_locked_until_" + nomor, 0);
  }

  public void clearPinLockedUntil(String nomor) {
    editor.remove("pin_locked_until_" + nomor).apply();
  }

  public void setThemeMode(int theme) {
    editor.putInt("dark_mode", theme).apply();
  }

  public int getThemeMode() {
    return preferences.getInt("dark_mode", AppCompatDelegate.MODE_NIGHT_NO);
  }
}

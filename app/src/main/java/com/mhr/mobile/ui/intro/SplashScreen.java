package com.mhr.mobile.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.mhr.mobile.api.listener.MokuCallback;
import com.mhr.mobile.api.request.RequestApp;
import com.mhr.mobile.api.request.RequestIntegrityChecker;
import com.mhr.mobile.api.response.ResponseSetting;
import com.mhr.mobile.databinding.SplashScreenBinding;
import com.mhr.mobile.ui.MainActivity;
import com.mhr.mobile.ui.navcontent.AkunMaintance;
import com.mhr.mobile.util.QiosPreferences;
import com.mhr.mobile.util.UtilsManager;

public class SplashScreen extends AppCompatActivity {
  private SplashScreenBinding binding;
  private UserSession session;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    binding = SplashScreenBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    session = UserSession.with(this);
    binding.tvVersion.setText("Version V" + UtilsManager.getVersionApp(this));
    loadSplash();
  }

  private void checkIntegrity() {
    RequestIntegrityChecker checker = new RequestIntegrityChecker(this);
    checker.StartIntegrity(
        new RequestIntegrityChecker.OnCheckComplete() {
          @Override
          public void onTokenReceived(String token) {
            checker.sendTokenToServer(
                token,
                new RequestIntegrityChecker.OnCheckServe() {
                  @Override
                  public void onCheck() {
                    finishAffinity();
                  }
                });
          }

          @Override
          public void onError(String error) {}
        });
  }

  private void loadSplash() {
    RequestApp requestApp = new RequestApp(this);
    requestApp.maintanceStatusRequest(
        new MokuCallback<ResponseSetting>() {
          @Override
          public void onDataLoading() {}

          @Override
          public void onDataValue(ResponseSetting data) {
            if (data.maintenance) {
              startActivity(new Intent(SplashScreen.this, AkunMaintance.class));
              finish();
            } else {
              continueSplash();
            }
          }

          @Override
          public void onDataError(String error) {
			  continueSplash();
		  }
        });
  }

  private void continueSplash() {
    new Handler(Looper.getMainLooper())
        .postDelayed(
            () -> {
              Intent intent;
              QiosPreferences pref = new QiosPreferences(this);
              if (session.isLoggedIn()) {
                if (pref.getPin()) {
                  intent = new Intent(this, UserPin.class);
                  intent.putExtra("mode", "login");
                  intent.putExtra("nomor", session.getNomor());
                  intent.putExtra("token", session.getToken());
                } else {
                  intent = new Intent(this, MainActivity.class);
                }
              } else {
                intent = new Intent(this, UserLogin.class);
              }

              startActivity(intent);
              finish();
              overridePendingTransition(0, 0);
            },
            1500);
  }
}

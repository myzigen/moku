package com.mhr.mobile.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AndroidTimes {
  private Context context;
  private CountDownTimer countDownTimer;
  private TextView textView;

  public AndroidTimes(Context context) {
    this.context = context;
  }

  public static AndroidTimes with(Context context) {
    return new AndroidTimes(context);
  }

  public AndroidTimes setText(TextView textView) {
    this.textView = textView;
    return this;
  }

  public void startBatasTopup(String batasPembayaran, String serverNow) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));

    try {
      Date batasDate = sdf.parse(batasPembayaran);
      Date serverDate = sdf.parse(serverNow);

      long millisUntil = batasDate.getTime() - serverDate.getTime(); // ❗Waktu server

      if (millisUntil > 0) {
        countDownTimer =
            new CountDownTimer(millisUntil, 1000) {
              public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long hours = seconds / 3600;
                long minutes = (seconds % 3600) / 60;
                long secs = seconds % 60;

                String timeLeft =
                    String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
                textView.setText("Sisa Waktu: " + timeLeft);
              }

              public void onFinish() {
                textView.setText("Waktu habis");
              }
            }.start();
      } else {
        textView.setText("Waktu sudah habis");
      }
    } catch (ParseException e) {
      textView.setText("Format tanggal salah");
      e.printStackTrace();
    }
  }

  public AndroidTimes cancelCountdown() {
    if (countDownTimer != null) {
      countDownTimer.cancel();
    }
    return this;
  }

  // Menampilkan Text Hari ini sesuai Waktu Indonesia
  public static String getTodayFormatted() {
    // Samakan format dengan headerDate, misalnya: "23 Juli 2025"
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id","ID"));
    return sdf.format(new Date());
  }
}

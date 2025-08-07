package com.mhr.mobile.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.mhr.mobile.R;

public class NotificationStatusTransaction {

  private static final String CHANNEL_ID = "transaksi_status_channel";
  private static final String CHANNEL_NAME = "Status Transaksi";
  private static final int NOTIFICATION_ID = 1;

  public static void showNotification(Context context, String title, String message) {
    NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    // Buat channel notifikasi (Android 8+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel =
          new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
      notificationManager.createNotificationChannel(channel);
    }

    // Buat notifikasi
    NotificationCompat.Builder builder =
        new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true);

    // Tampilkan notifikasi
    notificationManager.notify(NOTIFICATION_ID, builder.build());
  }
}

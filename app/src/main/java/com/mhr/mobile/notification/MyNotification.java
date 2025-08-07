package com.mhr.mobile.notification;

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.onesignal.notifications.IDisplayableMutableNotification;
import com.onesignal.notifications.INotificationReceivedEvent;
import com.onesignal.notifications.INotificationServiceExtension;

public class MyNotification implements INotificationServiceExtension {

  @Override
  public void onNotificationReceived(INotificationReceivedEvent event) {
    IDisplayableMutableNotification notification = event.getNotification();

    String refId = notification.getAdditionalData().optString("ref_id", null);
    String status = notification.getAdditionalData().optString("status", null);

    if (refId != null && status != null) {
      // Kirim broadcast ke MainActivity
      Intent intent = new Intent("com.mhr.NOTIF_UPDATE");
      intent.putExtra("ref_id", refId);
      intent.putExtra("status", status);
      LocalBroadcastManager.getInstance(event.getContext()).sendBroadcast(intent);
    }
  }
}

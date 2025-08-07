package com.mhr.mobile.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import com.mhr.mobile.util.AndroidViews;

public class DialogMokuAlert {
  private Activity context;

  public DialogMokuAlert(Activity context) {
    this.context = context;
  }

  public static DialogMokuAlert with(Activity context) {
    return new DialogMokuAlert(context);
  }

  public DialogMokuAlert TampilkanPesan(String title, String message) {
    new AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("Ok", null)
        .show();
    return this;
  }

  public DialogMokuAlert PesanAction(String title, String message, Class<?> target) {
    new AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("Top Up", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				GoToAction(target);
			}
		})
        .show();
    return this;
  }

  public void GoToAction(Class<?> targetActivity) {
    context.startActivity(new Intent(context, targetActivity));
  }

  public DialogMokuAlert TampilkanPesan(String title, String message, View v) {
    new AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(
            "Salin",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface arg0, int arg1) {
                AndroidViews.copyToClipboard(context, message, "Berhasil Di Salin",v);
              }
            })
        .show();
    return this;
  }
}

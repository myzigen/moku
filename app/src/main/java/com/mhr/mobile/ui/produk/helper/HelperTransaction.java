package com.mhr.mobile.ui.produk.helper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.mhr.mobile.api.response.ResponseTransaksi;
import com.mhr.mobile.api.response.ResponseTransaksiPasca;
import com.mhr.mobile.ui.dialog.LoadingDialogFragment;
import com.mhr.mobile.ui.navcontent.history.TransaksiSelesaiEvent;
import com.mhr.mobile.ui.navcontent.home.dashboard.DashboardTopup;
import com.mhr.mobile.ui.navcontent.home.dashboard.DashboardTopupEvent;
import com.mhr.mobile.ui.sheet.SheetErrorLayout;
import com.mhr.mobile.ui.status.StatusLayoutCallback;
import org.greenrobot.eventbus.EventBus;

public class HelperTransaction {
  private AppCompatActivity activity;
  private LoadingDialogFragment dialog;

  public HelperTransaction(AppCompatActivity activity) {
    this.activity = activity;
    this.dialog = new LoadingDialogFragment();
  }

  public static HelperTransaction with(AppCompatActivity activity) {
    return new HelperTransaction(activity);
  }

  public HelperTransaction GetResponse(ResponseTransaksi response) {
    if (response.getData() != null && response.getData().getRefId() != null) {
      dialog.showSuccess(() -> DetailTransaksi(response));

    } else {
      String message = response.getMessage();
      if (response.getData() != null && "server".equals(response.getData().getFrom())) {
        switch (response.getData().getType()) {
          case "saldo":
            int kurang = response.getData().getExpected() - response.getData().getAvailable();
            message =
                "Saldo kamu kurang Rp "
                    + kurang
                    + "\nHarga: "
                    + response.getData().getExpected()
                    + "\nSaldo tersedia: "
                    + response.getData().getAvailable();
            break;
          case "token_tidak_valid":
            message = "Sesi login kamu habis. Silakan login ulang.";
            break;
          case "input_kurang":
            message = "Data input kurang lengkap.";
            break;
        }
        DialogDismiss();

        DialogError();
      }
    }
    return this;
  }

  public HelperTransaction GetResponsePasca(ResponseTransaksiPasca response) {
    if (response.getData() != null && response.getData().getRefId() != null) {
      dialog.showSuccess(() -> DetailTransaksiPasca(response));

    } else {
      String message = response.getMessage();
      if (response.getData() != null && "server".equals(response.getData().getFrom())) {
        switch (response.getData().getType()) {
          case "saldo":
            int kurang = response.getData().getExpected() - response.getData().getAvailable();
            message =
                "Saldo kamu kurang Rp "
                    + kurang
                    + "\nHarga: "
                    + response.getData().getExpected()
                    + "\nSaldo tersedia: "
                    + response.getData().getAvailable();
            break;
          case "token_tidak_valid":
            message = "Sesi login kamu habis. Silakan login ulang.";
            break;
          case "input_kurang":
            message = "Data input kurang lengkap.";
            break;
        }
        DialogDismiss();

        DialogError();
      }
    }
    return this;
  }

  public void responseServer() {}

  public void DialogError() {
    new Handler(Looper.getMainLooper())
        .postDelayed(
            () -> {
              SheetErrorLayout errorLayout = new SheetErrorLayout();
              errorLayout.setTitle("Yaah, Saldo kamu kurang");
              errorLayout.setText("Saldo kamu tidak cukup untuk melakukan transaksi ini");
              errorLayout.setTextBtn("Isi Saldo");
              errorLayout.setToActivity(DashboardTopup.class);
              errorLayout.show(activity.getSupportFragmentManager(), "Topup");
            },
            2200);
  }

  public void DialogShow() {
    dialog.show(activity.getSupportFragmentManager(), "TAG");
  }

  public void DialogDismiss() {
    new Handler(Looper.getMainLooper())
        .postDelayed(
            () -> {
              dialog.dismiss();
            },
            2000);
  }

  private void DetailTransaksi(ResponseTransaksi response) {
    EventBus.getDefault().postSticky(new TransaksiSelesaiEvent());
    EventBus.getDefault().postSticky(new DashboardTopupEvent());
    Intent i = new Intent(activity, StatusLayoutCallback.class);
    Bundle args = new Bundle();
    i.putExtra("ref_id", response.getData().getRefId());
    activity.startActivity(i);
  }

  private void DetailTransaksiPasca(ResponseTransaksiPasca response) {
    EventBus.getDefault().postSticky(new TransaksiSelesaiEvent());
    EventBus.getDefault().postSticky(new DashboardTopupEvent());
    Intent i = new Intent(activity, StatusLayoutCallback.class);
    Bundle args = new Bundle();
    i.putExtra("ref_id", response.getData().getRefId());
    activity.startActivity(i);
  }
}

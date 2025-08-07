package com.mhr.mobile.ui.navcontent.home.dashboard;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.adapter.TopupAdapter;
import com.mhr.mobile.api.request.RequestTopup;
import com.mhr.mobile.api.request.duitku.DuitkuRequest;
import com.mhr.mobile.api.request.duitku.DuitkuResponse;
import com.mhr.mobile.api.response.ResponseTopup;
import com.mhr.mobile.databinding.ContentTopupBinding;
import com.mhr.mobile.ui.dialog.LoadingDialogFragment;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.sheet.SheetBankList;
import com.mhr.mobile.ui.status.StatusTopup;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import com.mhr.mobile.widget.button.OnActiveListener;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class DashboardTopup extends InjectionActivity implements TextWatcher {
  private ContentTopupBinding binding;
  private TopupAdapter adapter;
  private String paymentMethod, paymentName, imageUrl;
  private String fee;
  private int kodeUnik;
  private long expired = 60;
  private LoadingDialogFragment dialog;
  private boolean isProcessing = false;
  private boolean isJumlahDipilih = false;
  private boolean isBankDipilih = false;
  private List<ResponseTopup.TopupItem> topupList;
  private String errorMsg;

  @Override
  protected String getTitleToolbar() {
    return "Isi Saldo";
  }

  @Override
  public View onCreateQiosView(LayoutInflater i, ViewGroup c, Bundle b) {
    binding = ContentTopupBinding.inflate(getLayoutInflater());
    initialize();
    initUi();
    return binding.getRoot();
  }

  private void initialize() {
    dialog = new LoadingDialogFragment();
  }

  private void initUi() {
    initEditext(binding.editext);
    applyEditext();
    applyRecycler();
    String userName = session.getNama();
    String nomor = session.getNomor();
    String infoSaldo = pref.getSaldo();
    String normalNomor = FormatUtils.denormalizeNomor(nomor);
    binding.name.setText(userName);
    binding.nomor.setText("Isi saldo ke akun nomor " + normalNomor);
    binding.infoSaldo.setText("Saldo kamu saat ini: " + infoSaldo);

    binding.btnPilihBank.setOnClickListener(
        v -> {
          isClicked();

          SheetBankList sheet = new SheetBankList();
          sheet.setOnSheetClickListener(
              (nama, method, image, fee) -> {
                hideError();
                imageUrl = image;
                paymentMethod = method;
                this.fee = fee;
                paymentName = nama;
                binding.btnPilihBank.setStrokeColor(QiosColor.getActiveColor(this));
                binding.tvNamaBank.setText(nama);
                binding.type.setText(paymentMethod);
                Glide.with(this).load(imageUrl).into(binding.logo);
                binding.titleGanti.setVisibility(View.VISIBLE);
                isBankDipilih = true;
                checkButtonVisibility();
              });

          sheet.show(getSupportFragmentManager());
        });

    binding.btnPembayaran.setOnActiveListener(
        new OnActiveListener() {
          @Override
          public void onActive() {
            makePayment();
          }
        });
  }

  private void applyEditext() {
    binding.editext.addTextChangedListener(this);
    // binding.editext.setTextAppearance(android.R.style.TextAppearance_Large);
  }

  private void checkButtonVisibility() {
    if (isJumlahDipilih && isBankDipilih) {
      binding.btnPembayaran.setVisibility(View.VISIBLE);
      binding.infoBtn.setVisibility(View.VISIBLE);
    } else {
      binding.btnPembayaran.setVisibility(View.GONE);
      binding.infoBtn.setVisibility(View.GONE);
    }
  }

  private void applyRecycler() {
    binding.recyclerview.addItemDecoration(getSpacingItemDecoration(3, 50, true));
    binding.recyclerview.setLayoutManager(getGridLayoutManager(3));
    adapter = new TopupAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnClickListener(
        jumlah -> {
          hideKeyboard();
          hideError();
          adapter.setInputValid(true);
          binding.editext.setText((String.valueOf(jumlah.getJumlahTopup())));
          isJumlahDipilih = true;
          checkButtonVisibility();
        });
    listTopup();
  }

  private void listTopup() {
    RequestTopup topup = new RequestTopup(this);
    topup.setHeader(session.getToken(), session.getNomor());
    topup.requestTopup(
        new RequestTopup.Callback() {
          @Override
          public void onRequest() {}

          @Override
          public void onTopup(ResponseTopup topup) {
            List<ResponseTopup.TopupItem> item = topup.getData();
            adapter.setData(item);
            topupList = topup.getData();
          }

          @Override
          public void onFailure(String error) {
            showError(error);
          }
        });
  }

  private void showError(String error) {
    AndroidViews.ShowExpandError(error, binding.include.expandable, binding.include.cekUser);
  }

  private void hideError() {
    if (binding.include.expandable.isExpanded()) {
      binding.include.expandable.collapse();
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    if (s.length() == 0) {
      binding.editext.setTypeface(null, Typeface.NORMAL);
    } else {
      binding.editext.setTypeface(null, Typeface.BOLD);
    }
  }

  @Override
  public void afterTextChanged(Editable s) {
    String input = s.toString();
    if (input.isEmpty()) {
      adapter.setInputValid(false); // Set ke false agar warna kembali ke default
      adapter.resetSelectedPosition();
      isJumlahDipilih = false;
    } else {
      adapter.setInputValid(true);
      isJumlahDipilih = true;
    }
    checkButtonVisibility();
  }

  private void makePayment() {
    if (isProcessing) return; // Cegah klik berulang
    isProcessing = true;
    binding.btnPembayaran.setEnabled(false); // Nonaktifkan tombol

    String inputTopup = binding.editext.getText().toString();
    if (inputTopup.isEmpty()) {
      showError("Masukan Jumlah Topup");
      resetPaymentButton();
      return;
    }

    if (paymentMethod == null || paymentMethod.isEmpty()) {
      showError("Silahkan Pilih Pembayaran Terlebih Dahulu");
      binding.btnPilihBank.setStrokeColor(QiosColor.getErrorColor(this));
      resetPaymentButton();
      return;
    }

    int jumlahTopup = FormatUtils.parseRupiahToInt(inputTopup);
    int kodeUnik = 0;

    // Cari kode unik yang sesuai
    for (ResponseTopup.TopupItem item : topupList) {
      if (item.getJumlahTopup() == jumlahTopup) {
        kodeUnik = item.getKodeUnik();
        break;
      }
    }

    int jumlahTopupWithKodeUnik = jumlahTopup + kodeUnik;

    DuitkuRequest request = new DuitkuRequest(this);
    request.setAmount(jumlahTopupWithKodeUnik);
    request.setCustomerName(session.getNama());
    request.setCustomerPhone(session.getNomor());
    request.setCustomerEmail(session.getNama() + "@gmail.com");
    request.setPaymentMethod(paymentMethod);
    request.setPaymentName(paymentName);
    request.setDetail("Topup Saldo");
    request.setBiayaAdmin(fee);
    request.setKodeUnik(kodeUnik);
    request.requestTransaction(
        new DuitkuRequest.TransactionCallback() {
          @Override
          public void onTransactionStart() {
            dialog.show(getSupportFragmentManager(), "topup_dialog");
          }

          @Override
          public void onTransactionSuccess(DuitkuResponse data) {

            isProcessing = false;
            binding.btnPembayaran.setEnabled(true); // Aktifkan kembali
            EventBus.getDefault().postSticky(new DashboardTopupEvent());

            // Cek jika transaksi sebelumnya masih berlangsung
            if ("03".equals(data.getStatus())) {
              dialog.dismiss();
              showError(data.getMessage());
              return;
            }

            redirect(data);
          }

          @Override
          public void onTransactionFailed(String error) {
            dialog.dismiss();
            isProcessing = false;
            binding.btnPembayaran.setEnabled(true); // Aktifkan kembali
            showError(error);
          }
        });
  }

  private void resetPaymentButton() {
    isProcessing = false;
    binding.btnPembayaran.setEnabled(true);
  }

  private void redirect(DuitkuResponse response) {
    if (response.getPaymentUrl() != null) {
      if ("SA".equals(paymentMethod)) {
        dialog.showSuccess(() -> run(response));
      } else {
        dialog.showSuccess(() -> detailTopup(response));
      }
    }
  }

  private void run(DuitkuResponse data) {
    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getPaymentUrl()));
    startActivity(i);
  }

  private void detailTopup(DuitkuResponse data) {
    Intent intent = new Intent(this, StatusTopup.class);
    intent.putExtra("ref_id", data.getRefId()); // VA number bisa dianggap sebagai ref_id
    intent.putExtra("brand", paymentMethod); // atau gunakan nama bank/metode pembayarannya
    startActivity(intent);
  }
}

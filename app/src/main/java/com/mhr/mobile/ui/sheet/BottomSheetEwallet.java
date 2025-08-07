package com.mhr.mobile.ui.sheet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.BottomSheetEwalletBinding;
import com.mhr.mobile.inquiry.request.InquiryRequest;
import com.mhr.mobile.inquiry.response.InquiryResponse;
import com.mhr.mobile.util.Config;
import com.mhr.mobile.util.FormatUtils;

public class BottomSheetEwallet extends BottomSheetDialogFragment implements TextWatcher {
  private BottomSheetEwalletBinding binding;
  private OnProdukNominal callbackNominal;
  private boolean isFormatting = false; // Flag untuk mencegah loop

  public interface OnProdukNominal {
    void OnProdukNominal(double nominal);
  }

  public void setOnProdukNominal(OnProdukNominal nominal) {
    this.callbackNominal = nominal;
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = BottomSheetEwalletBinding.inflate(inflater, container, false);
    View view = binding.getRoot();

    // Set background dengan sudut membulat
    view.setBackgroundResource(R.drawable.shape_corners_top);

    // Inisialisasi UI
    initUi();
    return view;
  }

  private void initUi() {
    binding.editText.addTextChangedListener(this);
  }

  private void sendNominal(String bebasNominal) {
    binding.btnKirimNominal.setOnClickListener(
        v -> {
          double nominal =
              Double.parseDouble(
                  bebasNominal.replaceAll("[^\\d]", "")); // Remove non-digit characters
          if (callbackNominal != null) {
            callbackNominal.OnProdukNominal(nominal);
          }
          dismiss();
        });
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    String bebasNominal = s.toString().replaceAll("[^\\d]", ""); // Only numbers
    if (!TextUtils.isEmpty(bebasNominal)) {
      // Menyimpan posisi cursor sebelum teks diformat
      int cursorPos = binding.editText.getSelectionStart();

      // Menghindari format ulang berulang (loop)
      if (isFormatting) return;

      // Menandakan bahwa kita sedang memformat teks
      isFormatting = true;

      // Format input ke format Rp 5.000
      String formatRupiah = FormatUtils.formatRupiah(bebasNominal);

      // Cek apakah format sudah berbeda
      if (!formatRupiah.equals(binding.editText.getText().toString())) {
        // Set formatted text
        binding.editText.setText(formatRupiah);

        // Mengatur posisi kursor setelah teks diformat
        int newCursorPos = formatRupiah.length() - (bebasNominal.length() - cursorPos);
        newCursorPos = Math.max(0, Math.min(newCursorPos, formatRupiah.length()));
        binding.editText.setSelection(newCursorPos);
      }

      sendNominal(bebasNominal);

      // Mengatur flag kembali ke false setelah format selesai
      isFormatting = false;
    } else {
      binding.testNamaPelanggan.setText("Nominal harus berupa angka.");
    }
  }

  private void setUserInputNominal(String nomor) {
    // Inisialisasi request dengan parameter
    InquiryRequest request = new InquiryRequest(getActivity());
    request.setUsername(); // Ganti dengan username Anda
    request.setApiKey(); // Ganti dengan API key Anda
    request.setCodeProduk("SHOPEEPAY");
    request.setHp(nomor);

    // Kirim request
    request.startInquiryRequest(
        new InquiryRequest.InquiryCallback() {
          @Override
          public void onStartLoading() {}

          @Override
          public void onResponse(InquiryResponse response) {
            if (response != null && response.getData() != null) {
              InquiryResponse.Data data = response.getData();
              // Tampilkan hasil ke UI
            } else {
              binding.testNamaPelanggan.setText("Respons tidak valid.");
            }
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
  }
}

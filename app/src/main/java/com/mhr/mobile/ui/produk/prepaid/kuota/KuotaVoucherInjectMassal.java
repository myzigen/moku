package com.mhr.mobile.ui.produk.prepaid.kuota;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputLayout;
import com.mhr.mobile.R;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.FormPrepaidMassalBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.sheet.SheetInfoPrice;
import com.mhr.mobile.ui.produk.sheet.SheetPrepaidType;
import com.mhr.mobile.util.FormatUtils;
import java.util.ArrayList;
import java.util.List;

public class KuotaVoucherInjectMassal extends InjectionActivity {
  private FormPrepaidMassalBinding binding;
  private String kategori, brand;
  private int maxInput = 10;
  private LinearLayout wrapperEditext;
  private BottomSheetBehavior<View> behavior;
  private boolean isFormating;
  private EditText mEditText;
  private ResponsePricelist selectedProduk;

  @Override
  protected String getTitleToolbar() {
    return "Cetak Voucher Massal";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = FormPrepaidMassalBinding.inflate(layoutInflater, viewGroup, false);
    initialize();
    ui();
    return binding.getRoot();
  }

  private void initialize() {
    kategori = getAbsIntent("kategori");
    brand = getAbsIntent("brand");

    wrapperEditext = binding.editextWrapper;

    behavior = BottomSheetBehavior.from(binding.keyboard);
    behavior.setHideable(true);
    behavior.setDraggable(false);
    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
  }

  private void ui() {
    binding.infoSaldo.setText(pref.getSaldo());
    Glide.with(this).load(getAbsIntent("brand_icon")).into(binding.img);
    binding.btnRemoveVoucher.setOnClickListener(this::removeEditext);
    binding.btnAddVoucher.setOnClickListener(this::addVoucher);
    binding.pembayaran.btnLanjutBayar.setEnabled(false);
    binding.pembayaran.btnLanjutBayar.setText("Konfirmasi");
    binding.pembayaran.harga.setText("Rp0");
    binding.etQty.setText("1");

    addEditext();

    binding.etPilihOperator.setShowSoftInputOnFocus(false);
    binding.etPilihOperator.setOnTouchListener(
        (v, event) -> {
          if (event.getAction() == MotionEvent.ACTION_UP) {
            pilihOperator(v);
          }
          return false;
        });
  }

  private void addVoucher(View v) {
    // == Cek Apakah Produk Sudah Dipilih == //
    String produkDipilih = binding.etPilihOperator.getText().toString().trim();

    if (produkDipilih.isEmpty()) {
      showToast("Pilih Produk Terlebih Dahulu");
      return;
    }

    if (wrapperEditext.getChildCount() < maxInput) {
      addEditext();
      updateQty();
      updateHarga();
      validateAllInputs();
    } else {
      showSnackbar("Maksimal 10 Voucher");
    }
  }

  private void addEditext() {
    LayoutInflater inflater = LayoutInflater.from(this);
    View view = inflater.inflate(R.layout.inflate_text_input, wrapperEditext, false);
    wrapperEditext.addView(view);
    if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
      updateLayoutAboveKeyboard(true);
    } else {
      updateLayoutAboveKeyboard(false);
    }
    TextInputLayout inputLayout = view.findViewById(R.id.input_layout);
    EditText editText = view.findViewById(R.id.editext);
    mEditText = editText;
    mEditText.setShowSoftInputOnFocus(false);
    inputLayout.setPlaceholderText("1234-5678-90XX-X " + wrapperEditext.getChildCount());
    editextKarakter(mEditText);

    // Scroll ke bawah saat ditambahkan
    binding.nested.post(
        () -> {
          binding.nested.smoothScrollTo(0, wrapperEditext.getBottom());
        });

    EditorInfo editorInfo = new EditorInfo();
    InputConnection inputConnection = mEditText.onCreateInputConnection(editorInfo);
    if (inputConnection != null) {
      binding.keyboard.setInputConnection(inputConnection);
      binding.keyboard.setTargetEditText(mEditText);
    }

    // Saat disentuh
    mEditText.setOnTouchListener(
        (v, event) -> {
          if (event.getAction() == MotionEvent.ACTION_UP) {
            mEditText.requestFocus();

            binding.keyboard.setInputConnection(
                mEditText.onCreateInputConnection(new EditorInfo()));
            binding.keyboard.setTargetEditText(mEditText);

            if (behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
              behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
              updateLayoutAboveKeyboard(true);
            }
          }
          return false;
        });

    // Selesai klik di keyboard
    binding.keyboard.setOnSelesaiClickListener(
        v -> {
          if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            updateLayoutAboveKeyboard(false);
            EditText target = binding.keyboard.getTargetEditText();
            if (target != null) target.clearFocus();
          }
        });
    validateAllInputs();
  }

  private void updateLayoutAboveKeyboard(boolean showAboveKeyboard) {
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.root.getLayoutParams();

    if (showAboveKeyboard) {
      params.addRule(RelativeLayout.ABOVE, R.id.wrapper_keyboard);
    } else {
      params.removeRule(RelativeLayout.ABOVE);
      params.addRule(RelativeLayout.ABOVE, R.id.wrapperPembayaran);
    }

    binding.root.setLayoutParams(params);
  }

  private void removeEditext(View v) {
    if (wrapperEditext.getChildCount() > 1) {
      wrapperEditext.removeViewAt(wrapperEditext.getChildCount() - 1);
      updateQty();
      updateHarga();
      validateAllInputs();
    }
  }

  private void updateQty() {
    int count = wrapperEditext.getChildCount();
    binding.etQty.setText(String.valueOf(count));
  }

  private String gabungkanEditext() {
    List<String> voucher = new ArrayList<>();

    for (int i = 0; i < wrapperEditext.getChildCount(); i++) {
      View view = wrapperEditext.getChildAt(i);
      if (view instanceof EditText) {
        String code = ((EditText) view).getText().toString().trim();
        if (!code.isEmpty()) {
          voucher.add(code);
        }
      }
    }

    return TextUtils.join("\n", voucher);
  }

  private void aktivasiVoucher(String code, String refId) {}

  private void pilihOperator(View v) {
    SheetPrepaidType sheet = SheetPrepaidType.newInstance(kategori, brand);
    sheet.setOnSelectedLisetener(
        (model) -> {
          selectedProduk = model;
          binding.etPilihOperator.setText(model.getProductName());
          binding.ipProduk.setHelperText(model.getProductName());
          binding.ipProduk.setHelperTextEnabled(true);       
          updateHarga();
        });
    sheet.show(getSupportFragmentManager());
  }

  private void updateHarga() {
    int qty = wrapperEditext.getChildCount();
    int total = selectedProduk.getHargaJual() * qty;
    String totalFormatted = FormatUtils.formatRupiah(total);
    binding.pembayaran.harga.setText(totalFormatted);
	infoPembelian(selectedProduk, total);
  }

  private void setupFocusReset(EditText target, EditText... others) {
    target.setOnFocusChangeListener(
        (v, hasFocus) -> {
          if (hasFocus) {
            for (EditText other : others) {
              if (other.hasFocus()) other.clearFocus();
            }
          }
        });
  }

  private void editextKarakter(EditText editText) {
    editText.addTextChangedListener(
        new TextWatcher() {
          private boolean isFormatting;

          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {}

          @Override
          public void afterTextChanged(Editable s) {
            String validate = s.toString().trim();
            if (validate.isEmpty()) {
              binding.pembayaran.btnLanjutBayar.setEnabled(false);
            } else if (validate.length() >= 3) {
              binding.pembayaran.btnLanjutBayar.setEnabled(true);
            } else {

            }

            binding.pembayaran.btnLanjutBayar.setEnabled(false);

            if (isFormatting) return;

            isFormatting = true;

            String input = s.toString().replaceAll("-", "");
            StringBuilder formatted = new StringBuilder();

            for (int i = 0; i < input.length(); i++) {
              formatted.append(input.charAt(i));
              if ((i + 1) % 4 == 0 && (i + 1) != input.length()) {
                formatted.append("-");
              }
            }

            editText.removeTextChangedListener(this); // hindari loop
            editText.setText(formatted.toString());
            editText.setSelection(formatted.length()); // pindahkan kursor ke akhir
            editText.addTextChangedListener(this);

            isFormatting = false;
            validateAllInputs();
          }
        });
  }

  private void validateAllInputs() {
    boolean allValid = true;

    for (int i = 0; i < wrapperEditext.getChildCount(); i++) {
      View view = wrapperEditext.getChildAt(i);
      EditText editText = view.findViewById(R.id.editext);
      if (editText != null) {
        String value = editText.getText().toString().replaceAll("-", "").trim();
        if (value.length() < 3) {
          allValid = false;
          break;
        }
      }
    }

    binding.pembayaran.btnLanjutBayar.setEnabled(allValid);
  }

  private void infoPembelian(ResponsePricelist pricelist,int total) {
    binding.pembayaran.btnLanjutBayar.setOnClickListener(
        v -> {
          // == Cek Apakah Produk Sudah Dipilih == //
          String produkDipilih = binding.etPilihOperator.getText().toString().trim();
          if (produkDipilih.isEmpty()) {
			showSnackbar("Pilih Produk Terlebih Dahulu");
          }

          StringBuilder gabungkanNomor = new StringBuilder();
          for (int i = 0; i < wrapperEditext.getChildCount(); i++) {
            View view = wrapperEditext.getChildAt(i);
            EditText et = view.findViewById(R.id.editext);
            if (et != null) {
              String teks = et.getText().toString().trim();
              if (!teks.isEmpty()) {
                gabungkanNomor.append(teks).append("\n");
              }
            }
          }

          SheetInfoPrice sheet = SheetInfoPrice.newInstance(pricelist);
          sheet.setInfoNomor(gabungkanNomor.toString().trim());
		  sheet.setInfoHarga(total);  
          sheet.show(getSupportFragmentManager(), "KuotaVoucherInjectMassal");
        });
  }
}

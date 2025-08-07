package com.mhr.mobile.ui.sheet;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.mhr.mobile.R;
import com.mhr.mobile.api.listener.CheckNomorListener;
import com.mhr.mobile.api.request.RequestUsers;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.databinding.SheetPinBinding;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.ui.intro.UserLogin;
import com.mhr.mobile.ui.intro.UserSession;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.QiosColor;
import com.mhr.mobile.util.UtilsManager;

public class SheetPIN extends InjectionSheetFragment implements TextWatcher {
  private SheetPinBinding binding;
  private UserSession session;
  private String mode = "";
  private OnPinValidatedListener listener;
  private CountDownTimer pinLockTimer;
  private String fieldToUpdate = "";
  private boolean valueToUpdate = false;

  @Override
  protected String getSheetTitle() {
    return "Konfirmasi PIN ";
  }

  public static SheetPIN newInstance(String mode) {
    SheetPIN sheet = new SheetPIN();
    Bundle args = new Bundle();
    args.putString("mode", mode);
    sheet.setArguments(args);
    return sheet;
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetPinBinding.inflate(getLayoutInflater());

    if (getArguments() != null) {
      mode = getArguments().getString("mode", "");
    }
    session = UserSession.with(requireActivity());
    initEditext();
    if ("delete".equals(mode)) {
      applyPinDelete();
    } else if ("purchase".equals(mode)) {
      applyPinPurchase();
    } else if ("pin".equals(mode)) {
      applyPinPurchase();
    }

    return binding.getRoot();
  }

  private void initEditext() {
    binding.etPin.setShowSoftInputOnFocus(false);
    binding.etPin.addTextChangedListener(this);
    binding.keyboard.setEnabledBtnSelesai(false);
    EditorInfo editorInfo = new EditorInfo();
    InputConnection inputConnection = binding.etPin.onCreateInputConnection(editorInfo);
    if (inputConnection != null) {
      binding.keyboard.setInputConnection(inputConnection);
      binding.keyboard.setTargetEditText(binding.etPin);
    }
  }

  public void applyPinDelete() {
    binding.etPin.setOnTextCompleteListener(
        text -> {
          binding.keyboard.setOnSelesaiClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                  deleteAkun(text);
                  return;
                }
              });
          return false;
        });
  }

  public void applyPinPurchase() {
    binding.etPin.setOnTextCompleteListener(
        text -> {
          binding.keyboard.setOnSelesaiClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                  validatePinPurchase(text);
                  return;
                }
              });
          return false;
        });
  }

  private void validatePinPurchase(String pin) {
    RequestUsers request = RequestUsers.with(requireActivity());
    request.Token();
    request.setNomor(session.getNomor());
    request.setPin(pin);

    // Jika field dikirim, maka kirim field + value
    if (!fieldToUpdate.isEmpty()) {
      request.setField(fieldToUpdate);
      request.setValue(valueToUpdate);
    }

    request.ValidatePin(
        new CheckNomorListener() {
          @Override
          public void onRequest() {
            
          }

          @Override
          public void onCheckNomor(ResponseUsers users) {
            if ("success".equals(users.getStatus())) {
              
              dismiss();
              if (listener != null) {
                listener.onPinValidated();
              }
            } else if ("locked".equals(users.getStatus())) {
              long expiredAt = users.getExpiredAt();
              long now = System.currentTimeMillis() / 1000;
              if (expiredAt > now) {
                AndroidViews.startCountdownTimer(textView, expiredAt);
                failed(users.getMessage());
                binding.etPin.setEnabled(false);
              } else {
                binding.etPin.setEnabled(true);
                binding.keyboard.setEnabled(true);
                failed("Silakan coba lagi.");
              }
            } else {
              failed(users.getMessage());
            }
          }

          @Override
          public void onFailure(String error) {
            textView.setText(error);
          }
        });
  }

  private void deleteAkun(String pin) {
    RequestUsers request = RequestUsers.with(requireActivity());
    request.Token();
    request.setPin(pin);
    request.requestDeleteAccount(
        new RequestUsers.CallbackDelete() {
          @Override
          public void onRequest() {
           
          }

          @Override
          public void onSuccess(String message) {
            dismiss();
            textView.setText(message);
            session.logout();
            startActivity(new Intent(requireActivity(), UserLogin.class));
            requireActivity().finish();
          }

          @Override
          public void onFailed(String error) {
            textView.setText(error);
          }
        });
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    String input = s.toString().trim();
    if (input.isEmpty()) {
      binding.keyboard.setEnabledBtnSelesai(false);
      return;
    }
    if (input.length() >= 6) {
      binding.keyboard.setEnabledBtnSelesai(true);
      return;
    }
	
	binding.keyboard.setEnabledBtnSelesai(false);
  }

  private void failed(String message) {
    int errorColor = QiosColor.getColor(requireActivity(), R.color.status_canceled);
    binding.etPin.setFieldColor(errorColor);
    binding.etPin.setText("");
    binding.keyboard.setEnabledBtnSelesai(false);
    textView.setText(message);
    textView.setTextColor(errorColor);
    AndroidViews.animateShake(binding.etPin);
    UtilsManager.vibrateDevice(requireActivity());
  }

  public void setOnPinValidatedListener(OnPinValidatedListener listener) {
    this.listener = listener;
  }

  public interface OnPinValidatedListener {
    void onPinValidated();
  }

  public void setUpdateField(String field, boolean value) {
    this.fieldToUpdate = field;
    this.valueToUpdate = value;
  }
}

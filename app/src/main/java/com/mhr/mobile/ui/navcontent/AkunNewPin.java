package com.mhr.mobile.ui.navcontent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.mhr.mobile.R;
import com.mhr.mobile.api.listener.CheckNomorListener;
import com.mhr.mobile.api.request.RequestUsers;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.databinding.UserPinBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.QiosColor;
import com.mhr.mobile.util.UtilsManager;
import com.poovam.pinedittextfield.PinField;

public class AkunNewPin extends InjectionActivity {
  private UserPinBinding binding;

  private enum Step {
    OLD_PIN,
    NEW_PIN,
    CONFIRM_PIN
  }

  private Step currentStep = Step.OLD_PIN;
  private String oldPin = null;
  private String newPin = null;

  @Override
  public View onCreateQiosView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserPinBinding.inflate(inflater, viewGroup, false);
    binding.etPin.setShowSoftInputOnFocus(false);
    EditorInfo editorInfo = new EditorInfo();
    InputConnection inputConnection = binding.etPin.onCreateInputConnection(editorInfo);
    if (inputConnection != null) {
      binding.keyboard.setInputConnection(inputConnection);
      binding.keyboard.setTargetEditText(binding.etPin);
    }

    binding.title.setText("Masukan PIN Lama");
    binding.etPin.setOnTextCompleteListener(
        new PinField.OnTextCompleteListener() {
          @Override
          public boolean onTextComplete(String input) {
            switch (currentStep) {
              case OLD_PIN:
                validasiPin(input);
                break;
              case NEW_PIN:
                binding.title.setText("PIN Baru");
                newPin = input;
                break;
              case CONFIRM_PIN:
                break;
            }
            return false;
          }
        });
    return binding.getRoot();
  }

  private void validasiPin(String pin) {
    RequestUsers request = RequestUsers.with(this);
    request.setToken(session.getToken());
    request.setNomor(session.getNomor());
    request.setPin(pin);
    request.ValidatePin(
        new CheckNomorListener() {
          @Override
          public void onRequest() {
            dialog.show(getSupportFragmentManager(), "AkunValidasiPIN");
          }

          @Override
          public void onCheckNomor(ResponseUsers users) {
            if ("success".equals(users.getStatus())) {
              oldPin = pin;
              currentStep = Step.NEW_PIN;
            } else {
              failed("PIN Salah");
            }
          }

          @Override
          public void onFailure(String error) {
			  failed(error);
		  }
        });
  }

  private void usersNewPin(String pin) {}

  private void failed(String message) {
    dialog.dismiss();
    int errorColor = QiosColor.getColor(AkunNewPin.this, R.color.status_canceled);
	binding.etPin.setText("");
    binding.etPin.setFieldColor(errorColor);
    binding.strInputPin.setVisibility(View.VISIBLE);
    binding.strInputPin.setText(message);
	binding.strInputPin.setTextColor(errorColor);
    AndroidViews.animateShake(binding.etPin);
    UtilsManager.vibrateDevice(AkunNewPin.this);
  }
}

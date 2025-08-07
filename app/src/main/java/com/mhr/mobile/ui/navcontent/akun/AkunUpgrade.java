package com.mhr.mobile.ui.navcontent.akun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.UserUpgradeBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.sheet.SheetWilayah;

public class AkunUpgrade extends InjectionActivity {

  private UserUpgradeBinding binding;

  @Override
  protected String getTitleToolbar() {
    return "Upgrade Akun";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserUpgradeBinding.inflate(getLayoutInflater());
    binding.btn.setOnClickListener(
        v -> {
          SheetWilayah sheet = new SheetWilayah();
          sheet.setOnWilayahListener(
              (kec, kab, prov) -> {
                binding.editext.setText(kec + " " + kab + " " + prov);
              });

          sheet.show(getSupportFragmentManager());
        });
    return binding.getRoot();
  }
}

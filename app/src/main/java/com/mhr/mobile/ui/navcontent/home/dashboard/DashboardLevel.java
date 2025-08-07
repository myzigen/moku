package com.mhr.mobile.ui.navcontent.home.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.UserLevelBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;

// import static
// com.google.android.material.progressindicator.CircularProgressIndicator.INDETERMINATE_ANIMATION_TYPE_ADVANCE;

public class DashboardLevel extends InjectionActivity {
  private UserLevelBinding binding;

  @Override
  protected String getTitleToolbar() {
    return "Level Agen";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserLevelBinding.inflate(getLayoutInflater());
    // binding.progressMaster.setIndeterminateAnimationType(INDETERMINATE_ANIMATION_TYPE_ADVANCE);
    return binding.getRoot();
  }
}

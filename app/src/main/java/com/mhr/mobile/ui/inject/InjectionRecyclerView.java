package com.mhr.mobile.ui.inject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.InjectionRecyclerviewBinding;
import com.mikelau.shimmerrecyclerviewx.ShimmerRecyclerViewX;

public abstract class InjectionRecyclerView extends InjectionFragment {

  private InjectionRecyclerviewBinding binding;
  private ShimmerRecyclerViewX recyclerViewX;

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = InjectionRecyclerviewBinding.inflate(getLayoutInflater());
    recyclerViewX = binding.recyclerview;
    return binding.getRoot();
  }
}

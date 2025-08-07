package com.mhr.mobile.ui.sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mhr.mobile.adapter.pager.SheetListAdapter;
import com.mhr.mobile.databinding.SheetRecyclerviewBinding;
import com.mhr.mobile.model.ListSheet;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import java.util.ArrayList;
import java.util.List;

public class SheetFilterHistory extends InjectionSheetFragment {
  private SheetRecyclerviewBinding binding;
  private SheetListAdapter adapter;
  private List<ListSheet> mData = new ArrayList<>();

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetRecyclerviewBinding.inflate(getLayoutInflater());
    applyRecycler();
    return binding.getRoot();
  }

  private void applyRecycler() {
    binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireActivity()));
    mData = new ArrayList<>();
    adapter = new SheetListAdapter(mData);
    binding.recyclerview.setAdapter(adapter);
  }
}

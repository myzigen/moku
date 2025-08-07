package com.mhr.mobile.ui.produk.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.ItemFilterDataBinding;
import com.mhr.mobile.ui.produk.adapter.ProdukFilterAdapter.MyViewHolder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProdukFilterAdapter extends InjectAdapter<ProdukFilterAdapter.MyViewHolder> {
  private List<ResponsePricelist> allData;
  private OnFilterSelectedListener listener;
  private int selectedPosition = -1;

  public interface OnFilterSelectedListener {
    void onFilterSelected(ResponsePricelist filter);
  }

  public void setOnFilterSelected(OnFilterSelectedListener listener) {
    this.listener = listener;
  }

  public ProdukFilterAdapter(List<ResponsePricelist> data) {
    this.allData = AdapterHelper.removeDuplicateMasaAktif(data);
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ItemFilterDataBinding binding = ItemFilterDataBinding.inflate(inflater, parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    ResponsePricelist item = allData.get(position);
/*
    holder.binding.btnMasaAktif.setText(item.getMasaAktif());

    if (selectedPosition == position) {
      holder.binding.btnMasaAktif.setStrokeColor(
          ColorStateList.valueOf(Color.parseColor("#000000")));
    } else {
      holder.binding.btnMasaAktif.setStrokeColor(
          ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
    }

    holder.binding.btnMasaAktif.setOnClickListener(
        v -> {
          updateSelectedPosition(position); // Tetapkan filter yang dipilih
          // Kirimkan filter yang dipilih melalui listener
          if (listener != null) {
            listener.onFilterSelected(item);
          }
        });*/
  }

  public void notifyAdapterMasaAktif(List<ResponsePricelist> data) {
    this.allData = AdapterHelper.removeDuplicateMasaAktif(data);
    sortByMasaAktifPeriod();
    notifyDataSetChanged();
  }

  private void sortByMasaAktifPeriod() {
    Collections.sort(
        allData,
        new Comparator<ResponsePricelist>() {
          @Override
          public int compare(ResponsePricelist o1, ResponsePricelist o2) {
            int num1 = extractNumber(o1.getMasaAktif());
            int num2 = extractNumber(o2.getMasaAktif());
            return Integer.compare(num1, num2);
          }
        });
  }

  private int extractNumber(String s) {
    String num = s.replaceAll("[^0-9]", "");
    return Integer.parseInt(num);
  }
  
  public void updateSelectedPosition(int newSelectedPosition) {
    int previousPosition = selectedPosition;
    selectedPosition = newSelectedPosition;
    // Refresh tampilan item yang berubah
    notifyItemChanged(previousPosition);
    notifyItemChanged(newSelectedPosition);
  }

  @Override
  public int getItemCount() {
    return allData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemFilterDataBinding binding;

    public MyViewHolder(ItemFilterDataBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

package com.mhr.mobile.adapter.filter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.mhr.mobile.adapter.filter.FilterKuotaAdapter.FilterKuotaVH;
import com.mhr.mobile.databinding.ItemFilterDataBinding;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterKuotaAdapter extends RecyclerView.Adapter<FilterKuotaAdapter.FilterKuotaVH> {
  private List<String> mData; // Mengubah List<Product> menjadi List<String>
  private int selectedPosition = -1;
  private OnFilterKuotaListener listener;

  public interface OnFilterKuotaListener {
    void onFilterKuota(String filter); // Mengubah parameter dari Product ke String
  }

  public void setOnFilterKuota(OnFilterKuotaListener listener) {
    this.listener = listener;
  }

  public FilterKuotaAdapter(List<String> data) {
    this.mData = data;
  }

  @Override
  public FilterKuotaVH onCreateViewHolder(ViewGroup parent, int arg1) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ItemFilterDataBinding binding = ItemFilterDataBinding.inflate(inflater, parent, false);
    return new FilterKuotaVH(binding);
  }

  @Override
  public void onBindViewHolder(FilterKuotaVH holder, int position) {
    String filter = mData.get(position); // Mengambil data String
	/*
    holder.button.setText(filter); // Menampilkan String pada tombol

    if (selectedPosition == position) {
      holder.button.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#000000")));
    } else {
      holder.button.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
    }

    holder.button.setOnClickListener(
        v -> {
          updateSelectedPosition(position);
          if (listener != null) {
            listener.onFilterKuota(filter); // Mengirim String ke listener
          }
        });
		*/
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public void updateData(List<String> update) { // Mengupdate data dengan List<String>
    this.mData.clear();
    this.mData.addAll(update);
    notifyDataSetChanged();
  }
  // Menghapus duplikat pada List<String>
  private List<String> removeDuplicates(List<String> dataList) {
    Set<String> uniqueData = new HashSet<>(dataList);
    return new ArrayList<>(uniqueData);
  }

  public void updateSelectedPosition(int newSelectedPosition) {
    int previousPosition = selectedPosition;
    selectedPosition = newSelectedPosition;
    notifyItemChanged(previousPosition);
    notifyItemChanged(newSelectedPosition);
  }

  public class FilterKuotaVH extends RecyclerView.ViewHolder {
    //MaterialButton button;

    public FilterKuotaVH(ItemFilterDataBinding binding) {
      super(binding.getRoot());
      //button = binding.btnMasaAktif;
    }
  }
}

package com.mhr.mobile.ui.produk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.databinding.ItemFilterDataBinding;
import com.mhr.mobile.util.QiosColor;
import java.util.List;

public class GenericFilterAdapter extends InjectAdapter<GenericFilterAdapter.MyViewHolder> {
  private List<String> mData;
  private int selectedPosition = -1;
  private OnClickListener listener;

  public interface OnClickListener {
    void onClick(String selected);
  }

  public void setOnClickListener(OnClickListener listener) {
    this.listener = listener;
  }

  public GenericFilterAdapter(List<String> data) {
    this.mData = data;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ItemFilterDataBinding binding = ItemFilterDataBinding.inflate(inflater, parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    String item = mData.get(position);
    Context context = holder.binding.getRoot().getContext();
    if (item == null || item.trim().isEmpty()) {
      holder.binding.btnMasaAktif.setVisibility(View.GONE);
    } else {
      holder.binding.btnMasaAktif.setVisibility(View.VISIBLE);
      holder.binding.btnMasaAktif.setText(item);
    }

    holder.binding.btnMasaAktif.setOnClickListener(
        v -> {
          boolean isSame = (selectedPosition == position);
          updateSelectedPosition(position);
          if (!isSame) {
            listener.onClick(item); // Item baru dipilih
          } else {
            listener.onClick(null); // Item dibatalkan (dipilih lagi)
          }
        });

    // Tandai item yang dipilih
    if (selectedPosition == position) {
      //holder.binding.btnMasaAktif.setTextColor(QiosColor.getColor(context, R.color.me_color_text));
      //holder.binding.root.setBackgroundResource(R.drawable.corners_bg_white_card);
    } else {
      //holder.binding.root.setBackgroundResource(R.drawable.corners_bg_gray);
    }
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public void updateData(List<String> data) {
    this.mData.clear();
    this.mData.addAll(data);
    notifyDataSetChanged();
  }

  public void updateSelectedPosition(int newSelectedPosition) {
    int previousPosition = selectedPosition;

    if (selectedPosition == newSelectedPosition) {
      // Item yang sama diklik lagi → batalkan pilihan
      selectedPosition = -1;
      notifyItemChanged(previousPosition);
    } else {
      // Pilih item baru
      selectedPosition = newSelectedPosition;
      notifyItemChanged(previousPosition);
      notifyItemChanged(newSelectedPosition);
    }
  }

  public void resetSelectedPosition() {
    int previousPosition = selectedPosition;
    selectedPosition = -1;
    if (previousPosition != -1) {
      notifyItemChanged(previousPosition); // Perbarui tampilan posisi sebelumnya
    }
  }

  public String getSelectedItem() {
    if (selectedPosition >= 0 && selectedPosition < mData.size()) {
      return mData.get(selectedPosition);
    }
    return null;
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemFilterDataBinding binding;

    public MyViewHolder(ItemFilterDataBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

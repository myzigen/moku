package com.mhr.mobile.ui.navcontent.kelola.pelanggan;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.databinding.ItemFieldPelangganBinding;
import com.mhr.mobile.ui.navcontent.kelola.pelanggan.AdapterFieldPelanggan.MyViewHolder;
import java.util.List;

public class AdapterFieldPelanggan extends InjectAdapter<AdapterFieldPelanggan.MyViewHolder> {
  private List<FieldPelangganModel> mData;
  private OnEditClickListener listener;

  public interface OnEditClickListener {
    void onEditClick(String key);
  }

  public void setOnEditClickListener(OnEditClickListener listener) {
    this.listener = listener;
  }

  public AdapterFieldPelanggan(List<FieldPelangganModel> data) {
    this.mData = data;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new MyViewHolder(ItemFieldPelangganBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    FieldPelangganModel item = mData.get(position);
    holder.binding.labelField.setText(item.label);
    holder.binding.valueField.setText(item.getDisplayValue());

    holder.binding.btnEditField.setOnClickListener(
        v -> {
          if (listener != null) {
            listener.onEditClick(item.key);
          }
        });
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemFieldPelangganBinding binding;

    public MyViewHolder(ItemFieldPelangganBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

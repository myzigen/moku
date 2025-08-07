package com.mhr.mobile.ui.navcontent.kelola.pelanggan;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponsePelanggan;
import com.mhr.mobile.databinding.ItemDownlineBinding;
import com.mhr.mobile.util.FormatUtils;
import java.util.Collections;
import java.util.List;

public class PelangganAdapter extends InjectAdapter<PelangganAdapter.MyViewHolder> {
  private List<ResponsePelanggan.Data> mData;
  private OnItemClickListener listener;

  public interface OnItemClickListener {
    void onItemClick(ResponsePelanggan.Data data);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  public PelangganAdapter(List<ResponsePelanggan.Data> data) {
    this.mData = data;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new MyViewHolder(ItemDownlineBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    ResponsePelanggan.Data model = mData.get(position);
    holder.binding.initial.setText(FormatUtils.initialName(model.getNama()));
    holder.binding.infoNama.setText(model.getNama());
    holder.binding.infoNomor.setText(model.getNoHp());
    holder
        .binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (listener != null) {
                listener.onItemClick(model);
              }
            });
  }

  public void updateAdapter(List<ResponsePelanggan.Data> data) {
    if (data != null) {
      this.mData = data;
	  Collections.reverse(data); // balik urutan data
    }
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemDownlineBinding binding;

    public MyViewHolder(ItemDownlineBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

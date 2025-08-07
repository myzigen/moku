package com.mhr.mobile.ui.produk.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.ItemBankBinding;
import com.mhr.mobile.ui.produk.adapter.ProdukSheetPascaAdapter.MyViewHolder;
import java.util.List;

public class ProdukSheetPascaAdapter extends InjectAdapter<ProdukSheetPascaAdapter.MyViewHolder> {
  private List<ResponsePricelist> mData;
  private OnClickListener listener;

  public ProdukSheetPascaAdapter(List<ResponsePricelist> mData) {
    this.mData = mData;
  }

  public void setOnClickListener(OnClickListener listener) {
    this.listener = listener;
  }

  public interface OnClickListener {
    void onClick(ResponsePricelist model);
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ItemBankBinding binding = ItemBankBinding.inflate(inflater, parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    ResponsePricelist model = mData.get(position);

    holder.binding.produkName.setText(model.getProductName());
    Glide.with(holder.binding.getRoot().getContext())
        .load(model.getBrandIconUrl())
		.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
		.centerCrop()
        .into(holder.binding.icon);

    holder
        .binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (listener != null) {
                listener.onClick(model);
              }
            });
  }

  public void updateAdapter(List<ResponsePricelist> data) {
    this.mData.clear();
    this.mData.addAll(data);
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemBankBinding binding;

    public MyViewHolder(ItemBankBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

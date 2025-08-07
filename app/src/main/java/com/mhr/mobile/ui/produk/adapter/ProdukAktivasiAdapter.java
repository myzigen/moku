package com.mhr.mobile.ui.produk.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.ItemBankBinding;
import com.mhr.mobile.ui.produk.adapter.ProdukAktivasiAdapter.MyViewHolder;
import java.util.List;

public class ProdukAktivasiAdapter extends InjectAdapter<ProdukAktivasiAdapter.MyViewHolder> {
  private List<ResponsePricelist> mData;
  private OnClickListener listener;

  public interface OnClickListener {
    void onClick(ResponsePricelist item);
  }

  public void setOnClickListener(OnClickListener listener) {
    this.listener = listener;
  }

  public ProdukAktivasiAdapter(List<ResponsePricelist> data) {
    this.mData = AdapterHelper.removeDuplicateType(data);
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ItemBankBinding binding = ItemBankBinding.inflate(inflater, parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    ResponsePricelist item = mData.get(position);

    Glide.with(holder.binding.icon)
        .load(item.getBrandIconUrl())
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .centerCrop()
        .into(holder.binding.icon);
		
    holder.binding.produkName.setText(item.getType());
    holder.binding.tvSubtitle.setText(item.getDesc());
    holder.binding.cardView.setOnClickListener(
        v -> {
          if (listener != null) {
            listener.onClick(item);
          }
        });
  }

  public void updateAdapter(List<ResponsePricelist> data) {
    this.mData.clear();
    this.mData.addAll(AdapterHelper.removeDuplicateType(data));
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

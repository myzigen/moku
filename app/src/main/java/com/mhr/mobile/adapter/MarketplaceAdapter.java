package com.mhr.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mhr.mobile.adapter.MarketplaceAdapter.MyViewHolder;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.databinding.ItemMarketplaceBinding;
import com.mhr.mobile.manage.response.MarketplaceResponse;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.R;
import java.util.List;

public class MarketplaceAdapter extends InjectAdapter<MarketplaceAdapter.MyViewHolder> {

  private ItemMarketplaceBinding binding;
  private List<MarketplaceResponse.Data> mData;
  private OnProdukClickListener listener;

  public interface OnProdukClickListener {
    void onProdukClick(MarketplaceResponse.Data data);
  }

  public void setOnProdukClickListener(OnProdukClickListener listener) {
    this.listener = listener;
  }

  public MarketplaceAdapter(List<MarketplaceResponse.Data> data) {
    this.mData = data;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    binding = ItemMarketplaceBinding.inflate(LayoutInflater.from(context), parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    final MarketplaceResponse.Data model = mData.get(position);

    Glide.with(holder.binding.itemRoot.getContext())
        .load(model.getImageUrl().get(0))
        .placeholder(R.drawable.ic_no_image)
		.error(R.drawable.ic_no_image)
        .into(holder.binding.gambarProduk);

    holder.binding.namaProduk.setText(model.getProdukName());
    holder.binding.hargaProduk.setText(FormatUtils.formatRupiah(model.getHarga()));

    holder
        .binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (listener != null) {
                listener.onProdukClick(model);
              }
            });
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public void updateData(List<MarketplaceResponse.Data> data) {
    this.mData.clear();
    this.mData.addAll(data);
    notifyDataSetChanged();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemMarketplaceBinding binding;

    public MyViewHolder(ItemMarketplaceBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

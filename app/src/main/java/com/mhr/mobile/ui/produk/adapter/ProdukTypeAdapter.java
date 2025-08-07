package com.mhr.mobile.ui.produk.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.ItemUxListMovieBinding;
import java.util.List;

/**
 * Untuk Menampilkan Type Produk Digiflazz
 *
 * @param
 */
public class ProdukTypeAdapter extends InjectAdapter<ProdukTypeAdapter.MyViewHolder> {

  private List<ResponsePricelist> mData;
  private OnClickListener listener;

  public interface OnClickListener {
    void onClick(ResponsePricelist model);
  }

  public void setOnClickListener(OnClickListener listener) {
    this.listener = listener;
  }

  public ProdukTypeAdapter(List<ResponsePricelist> data) {
    this.mData = AdapterHelper.removeDuplicate(data);
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ItemUxListMovieBinding binding = ItemUxListMovieBinding.inflate(inflater, parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    ResponsePricelist model = mData.get(position);
    holder.binding.brand.setText(model.getBrand());
    Glide.with(holder.binding.root.getContext())
        .load(model.getBrandIconUrl())
		.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
		.centerCrop()
		.placeholder(R.drawable.ic_no_image)
        .into(holder.binding.brandIcon);

    holder.binding.root.setOnClickListener(
        v -> {
          if (listener != null) {
            listener.onClick(model);
          }
        });
  }

  public void updateProduk(List<ResponsePricelist> updateProduk) {
    if (updateProduk != null) {
      mData.clear();
      mData.addAll(AdapterHelper.removeDuplicate(updateProduk));
    }
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemUxListMovieBinding binding;

    public MyViewHolder(ItemUxListMovieBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

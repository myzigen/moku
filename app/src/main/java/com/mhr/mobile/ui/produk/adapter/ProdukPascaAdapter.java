package com.mhr.mobile.ui.produk.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.ItemListWithImageBinding;
import java.util.List;

/**
 * Untuk Menampilkan Daftar Produk Pascabayar Digiflazz
 *
 * @param
 */
public class ProdukPascaAdapter extends InjectAdapter<ProdukPascaAdapter.MyViewHolder> {
  private ItemListWithImageBinding binding;
  private List<ResponsePricelist> mData;
  private OnItemClickListener listener;

  public ProdukPascaAdapter(List<ResponsePricelist> data) {
    this.mData = data;
  }

  public interface OnItemClickListener {
    void OnItemClik(ResponsePricelist providerName);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    binding = ItemListWithImageBinding.inflate(inflater, parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    ResponsePricelist model = mData.get(position);

    holder.binding.produkName.setText(model.getProductName());
    Glide.with(holder.binding.getRoot().getContext())
        .load(model.getBrandIconUrl())
        .into(holder.binding.icon);
    setAktivProduk(model, holder);
  }

  private void setAktivProduk(ResponsePricelist model, MyViewHolder holder) {
    if (!model.getSellerProductStatus()) {
      holder.binding.getRoot().setEnabled(false);
      holder.binding.getRoot().setAlpha(0.5f);
    } else {
      holder.binding.getRoot().setEnabled(true);
      holder.binding.getRoot().setAlpha(1f);
    }

    holder
        .binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (listener != null) {
                listener.OnItemClik(model);
              }
            });
  }

  public void setNotifyProduk(List<ResponsePricelist> data) {
    if (data != null) {
      this.mData.clear();
      this.mData.addAll(data);
      // resetSelectedPosition();
    }
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemListWithImageBinding binding;

    public MyViewHolder(ItemListWithImageBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(ResponsePricelist model) {}
  }
}

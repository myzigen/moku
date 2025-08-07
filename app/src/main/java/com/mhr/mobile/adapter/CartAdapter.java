package com.mhr.mobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mhr.mobile.adapter.CartAdapter.MyViewHolder;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.cart.CartResponse;
import com.mhr.mobile.databinding.ItemCartBinding;
import com.mhr.mobile.util.FormatUtils;
import java.util.Iterator;
import java.util.List;

public class CartAdapter extends InjectAdapter<CartAdapter.MyViewHolder> {
  private List<CartResponse.CartItem> mData;
  private OnDeleteListener deleteListener;

  public interface OnDeleteListener {
    void onDelete(int id);
  }

  public void setOnDeleteListener(OnDeleteListener deleteListener) {
    this.deleteListener = deleteListener;
  }

  public CartAdapter(List<CartResponse.CartItem> data) {
    this.mData = data;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new MyViewHolder(ItemCartBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    CartResponse.CartItem item = mData.get(position);
    holder.binding.infoProduk.setText(item.nama_produk);
	holder.binding.infoNomor.setText(item.nomor_tujuan);
    holder.binding.infoPrice.setText(FormatUtils.formatRupiah(item.harga));
	Glide.with(holder.binding.image).load(item.brand_icon_url).into(holder.binding.image);
    holder.binding.delete.setOnClickListener(
        v -> {
          if (deleteListener != null) {
            deleteListener.onDelete(item.id);
          }
        });
  }

  public void removeItem(int id) {
    Iterator<CartResponse.CartItem> iterator = mData.iterator();
    while (iterator.hasNext()) {
      CartResponse.CartItem item = iterator.next();
      if (item.id == id) {
        iterator.remove();
        notifyDataSetChanged(); // update RecyclerView
        break;
      }
    }
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public void addAll(List<CartResponse.CartItem> data) {
    this.mData.clear();
    this.mData.addAll(data);
    notifyDataSetChanged();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemCartBinding binding;

    public MyViewHolder(ItemCartBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

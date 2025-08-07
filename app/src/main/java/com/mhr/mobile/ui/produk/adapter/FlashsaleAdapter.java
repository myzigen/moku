package com.mhr.mobile.ui.produk.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.ItemFlashsaleBinding;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import java.util.List;

public class FlashsaleAdapter extends InjectAdapter<FlashsaleAdapter.MyViewHolder> {
  private List<ResponsePricelist> mData;
  private OnClickListener listener;

  public interface OnClickListener {
    void onClick(ResponsePricelist model);
  }

  public void setOnClickListener(OnClickListener listener) {
    this.listener = listener;
  }

  public FlashsaleAdapter(List<ResponsePricelist> data) {
    this.mData = data;
  }

  public void setData(List<ResponsePricelist> data) {
    mData.clear();
    mData.addAll(data);
    notifyDataSetChanged();
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new MyViewHolder(ItemFlashsaleBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    ResponsePricelist item = mData.get(position);
    String produkName = item.getProductName();
    int maxChar = 18;
    String result = produkName.length() > maxChar ? produkName.substring(0, maxChar) + "..." : produkName;
    String gantiNamaData = item.getCategory();

    holder.binding.infoNama.setText(result);
    holder.binding.infoHarga.setText(FormatUtils.formatRupiah(item.getFlashPrice()));
    switch (item.getCategory()) {
      case "Data":
        gantiNamaData = "Paket Data";
        holder.binding.headerKategori.setText(gantiNamaData.toUpperCase());
        break;
      case "PLN":
        gantiNamaData = "Token Listrik";
        holder.binding.headerKategori.setText(gantiNamaData.toUpperCase());
        break;
      default:
        holder.binding.headerKategori.setText(item.getCategory().toUpperCase());
        break;
    }
    if ("Data".equalsIgnoreCase(item.getCategory())) {
      holder.binding.headerHarga.setText(item.getJumlahKuota());
    } else {
      holder.binding.headerHarga.setText(FormatUtils.formatRupiah(item.getHargaJual()));
    }

    Glide.with(holder.binding.root.getContext())
        .load(item.getBrandIconUrl())
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .centerCrop()
        .into(holder.binding.infoImg);

    QiosColor.applyDominantColorGradient(
        holder.binding.getRoot().getContext(), item.getBrandIconUrl(), holder.binding.dominan);

    holder.binding.root.setOnClickListener(
        v -> {
          if (listener != null) {
            listener.onClick(item);
          }
        });
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemFlashsaleBinding binding;

    public MyViewHolder(ItemFlashsaleBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

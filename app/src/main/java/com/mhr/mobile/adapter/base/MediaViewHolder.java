package com.mhr.mobile.adapter.base;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.mhr.mobile.databinding.ItemPricelistBinding;

public class MediaViewHolder extends RecyclerView.ViewHolder {

  public TextView produkName,
      produkDesc,
      produkNominal,
      produkDetails,
      hargaJual,
      produkActive,
      produkCategori,
      totalDiskon,
      hargaDiskon;
  public ImageView imgUrl;
  public MaterialCardView cardView;

  public MediaViewHolder(ItemPricelistBinding binding) {
    super(binding.getRoot());
    produkName = binding.productName;
	totalDiskon = binding.tvTotalDiskon;
	hargaDiskon = binding.tvHargaDiskon;
    hargaJual = binding.tvHargaJual;
  }
}

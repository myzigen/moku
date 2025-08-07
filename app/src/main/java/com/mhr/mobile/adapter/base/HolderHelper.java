package com.mhr.mobile.adapter.base;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import com.mhr.mobile.adapter.DataInternetAdapter;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.ui.produk.adapter.ProdukAdapter;
import com.mhr.mobile.util.FormatUtils;

public class HolderHelper {

  public static void applyHolder(
      Context context, ProdukAdapter.MyViewHolder holder, ResponsePricelist model) {
    holder.binding.productName.setText(model.getProductName());

    if (!model.getSellerProductStatus()) {
      holder.binding.productName.setAlpha(0.5f);
      holder.binding.tvHargaJual.setAlpha(0.5f);
      holder.binding.root.setEnabled(false);
      holder.binding.statusProduk.setVisibility(View.VISIBLE);
    } else {
      holder.binding.productName.setAlpha(1f);
      holder.binding.tvHargaJual.setAlpha(1f);
      holder.binding.root.setEnabled(true);
      holder.binding.statusProduk.setVisibility(View.GONE);
    }

    if (model.getDiskon() <= 0) {
      holder.binding.tvHargaJual.setText(FormatUtils.formatRupiah(model.getHargaJual()));
      holder.binding.tvHargaJual.setTypeface(null, Typeface.BOLD);
      // holder.binding.tvHargaJual.setTextColor(QiosColor.getColor(context, R.color.black));
      holder.binding.tvHargaJual.setPaintFlags(
          holder.binding.tvHargaJual.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
      holder.binding.tvTotalDiskon.setVisibility(View.GONE); // Sembunyikan diskon
      holder.binding.tvHargaDiskon.setVisibility(View.GONE); // Sembunyikan hargaDiskon
    } else {
      holder.binding.tvHargaJual.setText(FormatUtils.formatRupiah(model.getHargaJual()));
      holder.binding.tvHargaJual.setTypeface(null, Typeface.NORMAL);
      // holder.binding.tvHargaJual.setTextColor(QiosColor.getColor(context, R.color.black));
      holder.binding.tvHargaJual.setPaintFlags(
          holder.binding.tvHargaJual.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      holder.binding.tvHargaJual.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
      holder.binding.tvTotalDiskon.setVisibility(View.VISIBLE);
      holder.binding.tvTotalDiskon.setText((int) model.getDiskon() + " %");
      holder.binding.tvHargaDiskon.setText(FormatUtils.formatRupiah(model.getPriceAfterDiskon()));
      holder.binding.tvHargaDiskon.setVisibility(View.VISIBLE);
    }
  }

  public static void applyHolder(Context context, DataInternetAdapter.DataInternetVH holder, ResponsePricelist model) {
    holder.binding.txtNamaProduk.setText(model.getProductName());

    if (!model.getSellerProductStatus()) {
      holder.binding.txtNamaProduk.setAlpha(0.5f);
      holder.binding.tvHargaJual.setAlpha(0.5f);
      holder.binding.root.setEnabled(false);
	  holder.binding.infoKuota.setEnabled(false);
      holder.binding.statusProduk.setVisibility(View.VISIBLE);
    } else {
      holder.binding.txtNamaProduk.setAlpha(1f);
      holder.binding.tvHargaJual.setAlpha(1f);
      holder.binding.statusProduk.setVisibility(View.GONE);
      holder.binding.root.setEnabled(true);
	  holder.binding.infoKuota.setEnabled(true);
    }

    if (model.getDiskon() <= 0) {
      holder.binding.tvHargaJual.setText(FormatUtils.formatRupiah(model.getHargaJual()));
      holder.binding.tvHargaJual.setTypeface(null, Typeface.BOLD);
      // holder.binding.tvHargaJual.setTextColor(QiosColor.getColor(context, R.color.me_primary));
      holder.binding.tvHargaJual.setPaintFlags(
          holder.binding.tvHargaJual.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
      holder.binding.tvDiskon.setVisibility(View.GONE); // Sembunyikan diskon
      holder.binding.tvHargaDiskon.setVisibility(View.GONE); // Sembunyikan hargaDiskon
    } else {
      holder.binding.tvHargaJual.setText(FormatUtils.formatRupiah(model.getHargaJual()));
      holder.binding.tvHargaJual.setTypeface(null, Typeface.NORMAL);
      // holder.binding.tvHargaJual.setTextColor(QiosColor.getColor(context,
      // R.color.me_color_text_light));
      holder.binding.tvHargaJual.setPaintFlags(
          holder.binding.tvHargaJual.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      holder.binding.tvHargaJual.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
      holder.binding.tvDiskon.setVisibility(View.VISIBLE);
      holder.binding.tvDiskon.setText((int) model.getDiskon() + " %");
      holder.binding.tvHargaDiskon.setText(FormatUtils.formatRupiah(model.getPriceAfterDiskon()));
      holder.binding.tvHargaDiskon.setVisibility(View.VISIBLE);
    }
  }
}

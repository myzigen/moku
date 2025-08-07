package com.mhr.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.base.HolderHelper;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.ItemDataInternetBinding;
import com.mhr.mobile.ui.produk.adapter.AdapterHelper;
import com.mhr.mobile.util.QiosColor;
import java.util.ArrayList;
import java.util.List;

public class DataInternetAdapter extends InjectAdapter<DataInternetAdapter.DataInternetVH> {
  private List<ResponsePricelist> mData;
  private List<ResponsePricelist> mOriginalData; // Simpan data asli di sini
  private int selectedPosition = -1; // Posisi item yang dipilih
  private boolean isInputValid = false;
  public OnDataClickListener select;
  private String queryText = "";

  public boolean isInputValid() {
    return isInputValid;
  }

  public void setInputValid(boolean isInputValid) {
    this.isInputValid = isInputValid;
    notifyDataSetChanged();
  }

  public void setQueryText(String query) {
    this.queryText = query.toLowerCase();
  }

  public interface OnDataClickListener {
    void onDataClick(ResponsePricelist model);
  }

  public void setOnDataClickListener(OnDataClickListener selected) {
    this.select = selected;
  }
  // Constructor
  public DataInternetAdapter(List<ResponsePricelist> list) {
    this.mData = list;
    this.mOriginalData = new ArrayList<>(list); // Salin data asli
  }

  @NonNull
  @Override
  public DataInternetVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new DataInternetVH(ItemDataInternetBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull DataInternetVH holder, int position) {
    ResponsePricelist product = mData.get(position);
    Context context = holder.binding.getRoot().getContext();
    HolderHelper.applyHolder(context, holder, product);
    /*
       SpannableString highlight =
           AndroidViews.getHighlightedText(
               product.getProductName(),
               queryText,
               QiosColor.getColor(holder.itemView.getContext(), R.color.status_canceled));
       holder.namaProduk.setText(highlight);
    */

    if (product.getJumlahKuota().isEmpty()) {
      holder.binding.infoKuota.setText(product.getType());
    } else {
      holder.binding.infoKuota.setText(product.getJumlahKuota());
    }

    holder.binding.infoMasaAktif.setText("" + product.getMasaAktif());
    // Highlight jika item dipilih

    if (isInputValid) {
      if (selectedPosition == position) {
        holder.binding.root.setStrokeColor(QiosColor.getActiveColor(context));
      } else {
        holder.binding.root.setStrokeColor(QiosColor.getDisableColor(context));
      }
    } else {
      holder.binding.root.setStrokeColor(QiosColor.getDisableColor(context));
    }

    // Klik item untuk memperbarui posisi
    holder.binding.root.setOnClickListener(
        v -> {
          updateSelectedPosition(position);
          if (select != null) {
            select.onDataClick(product);
          }
        });
  }

  @Override
  public int getItemCount() {
    return mData != null ? mData.size() : 0;
  }

  // Mengambil data asli
  public List<ResponsePricelist> getOriginalData() {
    return mOriginalData;
  }

  // Menambahkan metode untuk mengatur data asli lagi jika perlu
  public void setOriginalData(List<ResponsePricelist> data) {
    this.mOriginalData = new ArrayList<>(data);
    this.mData = new ArrayList<>(data);
    sortProductListByPrice();
    notifyDataSetChanged();
  }

  // Menambahkan metode untuk memperbarui data
  public void updateData(List<ResponsePricelist> data) {
    this.mData.clear();
    this.mData.addAll(data);
    notifyDataSetChanged();
  }

  public void updateSelectedPosition(int newSelectedPosition) {
    int previousPosition = selectedPosition;
    selectedPosition = newSelectedPosition;
    // Refresh tampilan item yang berubah
    notifyItemChanged(previousPosition);
    notifyItemChanged(newSelectedPosition);
  }

  public void resetSelectedPosition() {
    int previousPosition = selectedPosition;
    selectedPosition = -1;
    if (previousPosition != -1) {
      notifyItemChanged(previousPosition); // Perbarui tampilan posisi sebelumnya
    }
  }

  public void sortProductListByPrice() {
    if (mData != null) {
      AdapterHelper.sortProductListByPrice(mData);
    }
  }

  public class DataInternetVH extends RecyclerView.ViewHolder {
    public ItemDataInternetBinding binding;

    public DataInternetVH(@NonNull ItemDataInternetBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

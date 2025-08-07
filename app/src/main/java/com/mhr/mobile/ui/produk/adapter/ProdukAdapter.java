package com.mhr.mobile.ui.produk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.base.HolderHelper;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.ItemPricelistBinding;
import com.mhr.mobile.util.QiosColor;
import java.util.ArrayList;
import java.util.List;

/**
 * Untuk Menampilkan Daftar Produk Digiflazz
 *
 * @param
 */
public class ProdukAdapter extends InjectAdapter<ProdukAdapter.MyViewHolder> {
  private List<ResponsePricelist> mData;
  private List<ResponsePricelist> mOriginalData; // Simpan data asli di sini
  private boolean isInputValid = false;
  private int selectedPosition = -1;
  private OnDataClickListener listener;

  public interface OnDataClickListener {
    void onDataClick(ResponsePricelist price);
  }

  public void setOnDataClickListener(OnDataClickListener listener) {
    this.listener = listener;
  }

  public boolean isInputValid() {
    return isInputValid;
  }

  public void setInputValid(boolean isInputValid) {
    this.isInputValid = isInputValid;
    notifyDataSetChanged();
  }

  public ProdukAdapter(List<ResponsePricelist> data) {
    this.mData = data;
    this.mOriginalData = new ArrayList<>(data);
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new MyViewHolder(ItemPricelistBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    ResponsePricelist model = mData.get(position);
    Context context = holder.binding.getRoot().getContext();
	
	HolderHelper.applyHolder(context, holder, model); 
    if (isInputValid) {
      if (selectedPosition == position) {
        holder.binding.root.setStrokeColor(QiosColor.getActiveColor(context));
      } else {
        holder.binding.root.setStrokeColor(QiosColor.getDisableColor(context));
      }
    } else {
      holder.binding.root.setStrokeColor(QiosColor.getDisableColor(context));
    }

    holder.binding.root.setOnClickListener(
        v -> {
          updateSelectedPosition(position);
          if (listener != null) {
            listener.onDataClick(model);
          }
        });
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public void updateSelectedPosition(int newSelectedPosition) {
    int previousPosition = selectedPosition;
    selectedPosition = newSelectedPosition;

    if (previousPosition != -1) {
      notifyItemChanged(previousPosition);
    }

    notifyItemChanged(newSelectedPosition);
  }

  public void resetSelectedPosition() {
    int previousPosition = selectedPosition;
    selectedPosition = -1;
    if (previousPosition != -1) {
      notifyItemChanged(previousPosition); // Perbarui tampilan posisi sebelumnya
    }
  }

  public void perbaruiData(List<ResponsePricelist> newData) {
    if (newData != null) {
      this.mData.clear();
      this.mData.addAll(newData);
      AdapterHelper.sortProductListByPrice(this.mData);
    }
    notifyDataSetChanged();
  }

  // Mengambil data asli
  public List<ResponsePricelist> getOriginalData() {
    return mOriginalData;
  }

  public void setOriginalData(List<ResponsePricelist> original) {
    if (original != null) {
      this.mOriginalData = new ArrayList<>(original);
      this.mData = new ArrayList<>(original);
      AdapterHelper.sortProductListByPrice(this.mData);
    }
    notifyDataSetChanged();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    public ItemPricelistBinding binding;

    public MyViewHolder(ItemPricelistBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

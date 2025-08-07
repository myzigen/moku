package com.mhr.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.TopupAdapter.MyViewHolder;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponseTopup;
import com.mhr.mobile.databinding.ItemTopupBinding;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import java.util.List;

public class TopupAdapter extends InjectAdapter<TopupAdapter.MyViewHolder> {
  private ItemTopupBinding binding;
  private List<ResponseTopup.TopupItem> mData;
  private int selectedPosition = -1;
  private boolean isInputValid = false;
  private OnClickListener listener;

  public interface OnClickListener {
    void onClick(ResponseTopup.TopupItem item);
  }

  public void setOnClickListener(OnClickListener listener) {
    this.listener = listener;
  }

  public void setInputValid(boolean isInputValid) {
    this.isInputValid = isInputValid;
    notifyDataSetChanged();
  }

  public TopupAdapter(List<ResponseTopup.TopupItem> data) {
    this.mData = data;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    binding = ItemTopupBinding.inflate(LayoutInflater.from(context), parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    ResponseTopup.TopupItem model = mData.get(position);
    Context context = holder.binding.getRoot().getContext();
    holder.binding.cardTextAmount.setText(FormatUtils.formatRupiah(model.getJumlahTopup()));
    if (isInputValid) {
      if (selectedPosition == position) {
        holder.binding.cardAmount.setStrokeColor(QiosColor.getActiveColor(context));
        holder.binding.cardTextAmount.setTextColor(QiosColor.getActiveColor(context));
      } else {
        holder.binding.cardAmount.setStrokeColor(QiosColor.getDisableColor(context));
        holder.binding.cardTextAmount.setTextColor(
            QiosColor.getColor(context, R.color.me_toolbar_title));
      }
    } else {
      holder.binding.cardAmount.setStrokeColor(QiosColor.getDisableColor(context));
      holder.binding.cardTextAmount.setTextColor(
          QiosColor.getColor(context, R.color.me_toolbar_title));
    }

    holder.binding.cardAmount.setOnClickListener(
        v -> {
          updateSelectedPosition(position);

          if (listener != null) {
            listener.onClick(model);
          }
        });
  }

  public void setData(List<ResponseTopup.TopupItem> data) {
    this.mData.clear();
    this.mData.addAll(data);
    notifyDataSetChanged();
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

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    public ItemTopupBinding binding;

    public MyViewHolder(ItemTopupBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

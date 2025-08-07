package com.mhr.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;
import com.bumptech.glide.Glide;
import com.mhr.mobile.adapter.TripayAdapter.MyViewHolder;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponseTripay;
import com.mhr.mobile.databinding.ItemBankBinding;
import com.mhr.mobile.util.FormatUtils;
import java.util.List;

public class TripayAdapter extends InjectAdapter<TripayAdapter.MyViewHolder> {

  private List<ResponseTripay.PaymentMethod> mData;
  private int selectedPosition = -1;
  private OnBankClickListener listener;

  public interface OnBankClickListener {
    void onBankClick(String iconUrl, String paymentMethod, String name);
  }

  public void setOnBankClickListener(OnBankClickListener listener) {
    this.listener = listener;
  }

  public TripayAdapter(List<ResponseTripay.PaymentMethod> data) {
    this.mData = data;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ItemBankBinding binding = ItemBankBinding.inflate(inflater, parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    ResponseTripay.PaymentMethod model = mData.get(position);

    boolean isSelected = position == selectedPosition;
    holder.binding.btnPilih.setVisibility(isSelected ? View.VISIBLE : View.GONE);

    ConstraintSet set = new ConstraintSet();
    set.clone(holder.binding.getRoot());

    if (isSelected) {
      set.clear(holder.binding.cardView.getId(), ConstraintSet.END);
      set.connect(
          holder.binding.cardView.getId(),
          ConstraintSet.END,
          holder.binding.btnPilih.getId(),
          ConstraintSet.START,
          8);

      holder.binding.btnPilih.setAlpha(0f);
      holder.binding.btnPilih.setTranslationX(50f);
      holder.binding.btnPilih.animate().alpha(1f).translationX(0f).setDuration(300).start();
    } else {
      set.clear(holder.binding.cardView.getId(), ConstraintSet.END);
      set.connect(
          holder.binding.cardView.getId(),
          ConstraintSet.END,
          ConstraintSet.PARENT_ID,
          ConstraintSet.END,
          0);
    }

    TransitionManager.beginDelayedTransition(holder.binding.getRoot());
    set.applyTo(holder.binding.getRoot());

    int fee = model.getFeeCustomer().getFlat();
    holder.binding.produkName.setText(model.getName());
    holder.binding.tvSubtitle.setText("Biaya Layanan " + FormatUtils.formatRupiah(fee));

    Glide.with(holder.binding.getRoot().getContext())
        .load(model.getIconUrl())
        .into(holder.binding.icon);

    holder.binding.cardView.setOnClickListener(
        v -> {
          if (selectedPosition == position) return;
          int prev = selectedPosition;
          selectedPosition = position;

          notifyItemChanged(prev);
          notifyItemChanged(position);
          if (listener != null) {
            listener.onBankClick(model.getIconUrl(), model.getCode(), model.getName());
          }
        });
  }

  public void updateAdapter(List<ResponseTripay.PaymentMethod> data) {
    mData.clear();
    mData.addAll(data);
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemBankBinding binding;

    public MyViewHolder(ItemBankBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

package com.mhr.mobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mhr.mobile.adapter.DuitkuAdapter.MokuVH;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.request.duitku.DuitkuResponse;
import com.mhr.mobile.databinding.ItemBankBinding;
import com.mhr.mobile.util.FormatUtils;
import java.util.List;

public class DuitkuAdapter extends InjectAdapter<DuitkuAdapter.MokuVH> {

  private List<DuitkuResponse.PaymentMethod> mData;
  private OnBankClickListener listener;

  public interface OnBankClickListener {
    void onBankClick(
        String paymentName, String paymentMethod, String paymentImage, String totalFee);
  }

  public void setOnBankClickListener(OnBankClickListener listener) {
    this.listener = listener;
  }

  public DuitkuAdapter(List<DuitkuResponse.PaymentMethod> data) {
    this.mData = data;
  }

  @Override
  public MokuVH onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new MokuVH(ItemBankBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(MokuVH holder, int position) {
    DuitkuResponse.PaymentMethod item = mData.get(position);
    Glide.with(holder.binding.icon).load(item.paymentImage).into(holder.binding.icon);
    holder.binding.produkName.setText(item.paymentName);
    String totalFee = FormatUtils.formatRupiah(item.totalFee);
    holder.binding.tvSubtitle.setText("Biaya Layanan " + totalFee);

    holder.binding.cardView.setOnClickListener(
        v -> {
          listener.onBankClick(
              item.paymentName, item.paymentMethod, item.paymentImage, item.totalFee);
        });
  }

  public void setData(List<DuitkuResponse.PaymentMethod> data) {
    this.mData.clear();
    this.mData.addAll(data);
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MokuVH extends RecyclerView.ViewHolder {
    ItemBankBinding binding;

    public MokuVH(ItemBankBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

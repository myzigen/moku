package com.mhr.mobile.ui.navcontent.downline.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.databinding.ItemDownlineBinding;
import com.mhr.mobile.ui.navcontent.downline.adapter.DownlineAdapter.MyViewHolder;
import com.mhr.mobile.util.FormatUtils;
import java.util.List;

public class DownlineAdapter extends InjectAdapter<DownlineAdapter.MyViewHolder> {
  private List<ResponseUsers.Downline> mData;
  private OnClickItemListener listener;

  public interface OnClickItemListener {
    void onClickItem(int position);
  }

  public void setClickItemListener(OnClickItemListener listener) {
    this.listener = listener;
  }

  public DownlineAdapter(List<ResponseUsers.Downline> data) {
    this.mData = data;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ItemDownlineBinding binding = ItemDownlineBinding.inflate(inflater, parent, false);

    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    ResponseUsers.Downline item = mData.get(position);
    String maskingNomor = FormatUtils.maskingNomor(item.getNomor());
    holder.binding.initial.setText(FormatUtils.initialName(item.getNama()));
    holder.binding.infoNomor.setText(maskingNomor);
    holder.binding.infoNama.setText(item.getNama());

    holder
        .binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (listener != null) {
                listener.onClickItem(position);
              }
            });
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public void updateDownlineAdapter(List<ResponseUsers.Downline> data) {
    this.mData.clear();
    this.mData.addAll(data);
    notifyDataSetChanged();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemDownlineBinding binding;

    public MyViewHolder(ItemDownlineBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

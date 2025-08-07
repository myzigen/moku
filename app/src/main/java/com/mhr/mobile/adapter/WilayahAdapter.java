package com.mhr.mobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.WilayahAdapter.MokuVH;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.databinding.ItemBankBinding;
import com.mhr.mobile.model.Wilayah;
import java.util.List;

public class WilayahAdapter extends InjectAdapter<WilayahAdapter.MokuVH> {

  private List<Wilayah.Item> mData;
  private OnSelectedListener listener;

  public interface OnSelectedListener {
    void onSelected(Wilayah.Item item);
  }

  public void setOnSelectedListener(OnSelectedListener listener) {
    this.listener = listener;
  }

  public WilayahAdapter(List<Wilayah.Item> data) {
    this.mData = data;
  }

  @Override
  public MokuVH onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new MokuVH(ItemBankBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(MokuVH holder, int position) {
    Wilayah.Item wilayah = mData.get(position);
    holder.binding.produkName.setText(wilayah.nama);
	holder.binding.tvSubtitle.setText(wilayah.kabupaten + " " + wilayah.provinsi);
	holder.binding.icon.setVisibility(View.GONE);
	holder.binding.getRoot().setOnClickListener(v -> {
		listener.onSelected(wilayah);
	});
  }

  public void setData(List<Wilayah.Item> data) {
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

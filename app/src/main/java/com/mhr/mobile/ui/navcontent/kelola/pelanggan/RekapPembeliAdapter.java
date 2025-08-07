package com.mhr.mobile.ui.navcontent.kelola.pelanggan;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.databinding.ItemBankBinding;
import com.mhr.mobile.model.RekapNomorModel;
import com.mhr.mobile.ui.navcontent.kelola.pelanggan.RekapPembeliAdapter.VH;
import com.mhr.mobile.util.FormatUtils;
import java.util.List;

public class RekapPembeliAdapter extends InjectAdapter<RekapPembeliAdapter.VH> {
  List<RekapNomorModel> mData;

  public RekapPembeliAdapter(List<RekapNomorModel> data) {
    this.mData = data;
  }

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new VH(ItemBankBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {
	  RekapNomorModel model = mData.get(position);
	  holder.binding.produkName.setText(model.getCustomerNo());
	  holder.binding.tvSubtitle.setText(FormatUtils.formatRupiah(model.getTotalPembelian()));
	  
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class VH extends RecyclerView.ViewHolder {
    ItemBankBinding binding;

    public VH(ItemBankBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

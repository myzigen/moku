package com.mhr.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mhr.mobile.databinding.ItemBankBinding;
import com.mhr.mobile.databinding.ItemHeaderBinding;
import com.mhr.mobile.model.Bank;
import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context context;
  private List<Bank.BankHeader> mData;

  private OnBankClickListener listener;

  public interface OnBankClickListener {
    void onBankClick(String logoUrl, String codeProduk, String name, String norek, String type);
  }

  public void setOnBankClickListener(OnBankClickListener listener) {
    this.listener = listener;
  }

  public BankAdapter(Context ctx, List<Bank.BankHeader> items) {
    this.context = ctx;
    this.mData = items;
  }

  @Override
  public int getItemViewType(int position) {
    return mData.get(position).getType(); // Tentukan tipe item
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    if (viewType == Bank.BankHeader.TYPE_HEADER) {
      return new HeaderViewHolder(ItemHeaderBinding.inflate(inflater, parent, false));
    } else {
      return new BankViewHolder(ItemBankBinding.inflate(inflater, parent, false));
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    Bank.BankHeader item = mData.get(position);

    if (holder instanceof HeaderViewHolder) {
      ((HeaderViewHolder) holder).binding.tvHeader.setText(item.getHeaderTitle());
    } else if (holder instanceof BankViewHolder) {
      Bank model = item.getBank();
      if (model != null) {
        BankViewHolder bankHolder = (BankViewHolder) holder;
        if (model.getType().equalsIgnoreCase("VA")) {
          bankHolder.binding.produkName.setText("VA " + model.getBankName());
        } else {
          bankHolder.binding.produkName.setText(model.getBankName());
        }
        if (position == 1) {
          bankHolder.binding.tvSubtitle.setText("Dana/ShopeePay/Gopay");
        } else {
          bankHolder.binding.tvSubtitle.setText("Biaya Admin Rp 2.000");
        }
        Glide.with(context).load(model.getLogoUrl()).into(bankHolder.binding.icon);
        bankHolder
            .binding
            .getRoot()
            .setOnClickListener(
                v -> {
                  if (listener != null) {
                    listener.onBankClick(
                        model.getLogoUrl(),
                        model.getCodeProduk(),
                        model.getBankName(),
                        model.getNoRekening(),
                        model.getType());
                  }
                });
      }
    }
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public void updateData(List<Bank.BankHeader> data) {
    this.mData.clear();
    this.mData.addAll(data);
    notifyDataSetChanged();
  }

  // ViewHolder untuk header
  public static class HeaderViewHolder extends RecyclerView.ViewHolder {
    public ItemHeaderBinding binding;

    public HeaderViewHolder(ItemHeaderBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  // ViewHolder untuk item bank
  public static class BankViewHolder extends RecyclerView.ViewHolder {
    public ItemBankBinding binding;

    public BankViewHolder(ItemBankBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

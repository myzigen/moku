package com.mhr.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponseHistory;
import com.mhr.mobile.databinding.ItemFooterLoadingBinding;
import com.mhr.mobile.databinding.ItemHeaderBinding;
import com.mhr.mobile.databinding.ItemStatusTransaksiBinding;
import com.mhr.mobile.model.RiwayatHeader;
import com.mhr.mobile.ui.produk.adapter.AdapterHelper;
import com.mhr.mobile.util.FormatUtils;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends InjectAdapter<RecyclerView.ViewHolder> {
  private List<RiwayatHeader> mData;
  private static final int VIEW_TYPE_ITEM = 0;
  private static final int VIEW_TYPE_LOADING = 1;
  private static final int VIEW_TYPE_HEADER = 2;
  private boolean showLoading = false;
  private boolean selesaiDimuat = false;
  private String lastHeaderDate = ""; // untuk mencegah duplikasi header saat endless scroll
  private OnHistoryClickListener listener;

  public interface OnHistoryClickListener {
    void onClick(ResponseHistory.Data data);
  }

  public void setOnHistoryClickListener(OnHistoryClickListener listener) {
    this.listener = listener;
  }

  public HistoryAdapter(List<ResponseHistory.Data> data) {
    this.mData = groupWithHeaders(data);
  }

  public void setLastPage(boolean selesai) {
    if (mData.isEmpty()) {
      showLoadingFooter(false); // Jangan tampilkan footer kalau belum ada data
      this.selesaiDimuat = false;
      return;
    }

    this.selesaiDimuat = selesai;

    if (selesai && mData.size() <= 10) {
      // Tidak perlu tampilkan "semua data dimuat" kalau cuma ada 10 atau kurang
      showLoadingFooter(false);
    } else if (selesai) {
      showLoadingFooter(true);
      notifyItemChanged(mData.size());
    } else {
      showLoadingFooter(false);
    }
  }

  public void showLoadingFooter(boolean show) {
    if (this.showLoading == show) return;
    this.showLoading = show;
    if (show) {
      if (mData.size() > 0) {
        notifyItemInserted(mData.size());
      }
    } else {
      if (mData.size() > 0) {
        notifyItemRemoved(mData.size());
      }
    }
  }

  public void hideLoadingFooter() {
    this.showLoading = false;
    notifyItemRemoved(mData.size());
  }

  private List<RiwayatHeader> groupWithHeaders(List<ResponseHistory.Data> originalList) {
    List<RiwayatHeader> result = new ArrayList<>();

    for (ResponseHistory.Data item : originalList) {
      String fullTanggal = item.getTanggal().trim();
      String[] splitTanggal = fullTanggal.split(" - ");
      String headerDate = splitTanggal.length > 0 ? splitTanggal[0] : fullTanggal;

      if (!headerDate.equals(lastHeaderDate)) {
        result.add(new RiwayatHeader(headerDate));
        lastHeaderDate = headerDate;
      }

      result.add(new RiwayatHeader(item));
    }

    return result;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());

    if (viewType == VIEW_TYPE_LOADING) {
      return new LoadingViewHolder(ItemFooterLoadingBinding.inflate(inflater, parent, false));
    } else if (viewType == VIEW_TYPE_HEADER) {
      return new HeaderViewHolder(ItemHeaderBinding.inflate(inflater, parent, false));
    } else {
      return new MyViewHolder(ItemStatusTransaksiBinding.inflate(inflater, parent, false));
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof MyViewHolder) {
      ResponseHistory.Data model = mData.get(position).getData();
      MyViewHolder viewHolder = (MyViewHolder) holder;

      viewHolder.binding.price.setText(FormatUtils.formatRupiah(model.getJumlah()));
      viewHolder.binding.tvDate.setText(model.getTanggal());
      viewHolder.binding.customerId.setText(model.getKeterangan());
      String namaProduk = model.getJenis();
      if ("Data".equalsIgnoreCase(namaProduk)) {
        namaProduk = "Paket Data";
        viewHolder.binding.tvProduk.setText(namaProduk);
      } else {
        viewHolder.binding.tvProduk.setText(namaProduk);
      }

      String kategori = model.getKategori();

      if ("deposit".equalsIgnoreCase(kategori)) {
        Glide.with(viewHolder.binding.brandIcon)
            .load("https://api.qiospro.my.id/assets/logo/ic-wallet2.png")
            .into(viewHolder.binding.brandIcon);
      } else if (kategori != null && !kategori.isEmpty()) {
        Glide.with(viewHolder.binding.brandIcon)
            .load(model.getBrandIconUrl())
            .into(viewHolder.binding.brandIcon);
      } else {
        // Kategori null atau kosong, bisa pakai icon default atau kosongkan image
        viewHolder.binding.brandIcon.setImageResource(R.drawable.ic_no_image);
      }

      AdapterHelper.setStatusColor(
          viewHolder.binding.tvStatus,
          model.getStatus(),
          viewHolder.binding.getRoot().getContext());

      viewHolder.binding.root.setOnClickListener(
          v -> {
            if (listener != null) listener.onClick(model);
          });

      // hilangkan divider diposisi terakhir
      boolean isLastInSection = false;
      if (position + 1 >= mData.size()) {
        isLastInSection = true;
      } else {
        RiwayatHeader next = mData.get(position + 1);
        if (next.getType() == RiwayatHeader.TYPE_HEADER) {
          isLastInSection = true;
        }
      }

      viewHolder.binding.divider.setVisibility(isLastInSection ? View.GONE : View.VISIBLE);

    } else if (holder instanceof HeaderViewHolder) {
      ((HeaderViewHolder) holder).bind(mData.get(position).getHeaderDate());

    } else if (holder instanceof LoadingViewHolder) {
      ((LoadingViewHolder) holder).bind(selesaiDimuat);
    }
  }

  public void updateAdapter(List<ResponseHistory.Data> data) {
    lastHeaderDate = ""; // reset agar data baru bisa mulai dari awal
    mData = groupWithHeaders(data);
    notifyDataSetChanged();
  }

  public void endlessData(List<ResponseHistory.Data> data) {
    if (data != null && !data.isEmpty()) {
      // Hapus footer sebelum tambah data
      if (showLoading) {
        notifyItemRemoved(mData.size());
      }

      List<RiwayatHeader> newItems = groupWithHeaders(data);
      int start = mData.size();
      mData.addAll(newItems);
      notifyItemRangeInserted(start, newItems.size());

      // Tambahkan footer lagi
      if (showLoading) {
        notifyItemInserted(mData.size());
      }
    }
  }

  public boolean containsRefId(String refId) {
    for (RiwayatHeader item : mData) {
      if (item.getType() == RiwayatHeader.TYPE_ITEM) {
        ResponseHistory.Data data = item.getData();
        if (data != null && data.getRefId().equals(refId)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public int getItemCount() {
    return mData == null ? 0 : mData.size() + (showLoading ? 1 : 0);
  }

  @Override
  public int getItemViewType(int position) {
    if (showLoading && position == mData.size()) {
      return VIEW_TYPE_LOADING;
    }
    RiwayatHeader item = mData.get(position);
    return item.getType() == RiwayatHeader.TYPE_HEADER ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
  }

  public static class HeaderViewHolder extends RecyclerView.ViewHolder {
    ItemHeaderBinding binding;

    public HeaderViewHolder(ItemHeaderBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(String date) {
      binding.tvHeader.setText(date);
    }
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {
    ItemStatusTransaksiBinding binding;

    public MyViewHolder(ItemStatusTransaksiBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  public static class LoadingViewHolder extends RecyclerView.ViewHolder {
    ItemFooterLoadingBinding binding;

    public LoadingViewHolder(ItemFooterLoadingBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(boolean isLastPage) {
      if (isLastPage) {
        binding.footerText.setText("Semua data telah dimuat");
        binding.progress.setVisibility(View.GONE);
      } else {
        binding.footerText.setText("Memuat data...");
        binding.progress.setVisibility(View.VISIBLE);
      }
    }
  }
}

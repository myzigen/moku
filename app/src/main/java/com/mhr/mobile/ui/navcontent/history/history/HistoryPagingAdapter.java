package com.mhr.mobile.ui.navcontent.history.history;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.api.response.ResponseHistory;
import com.mhr.mobile.databinding.ItemFooterLoadingBinding;
import com.mhr.mobile.databinding.ItemHeaderBinding;
import com.mhr.mobile.databinding.ItemStatusTransaksiBinding;
import com.mhr.mobile.model.RiwayatHeader;
import com.mhr.mobile.ui.produk.adapter.AdapterHelper;
import com.mhr.mobile.util.AndroidTimes;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.widget.recyclerview.PaginationAdapterCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryPagingAdapter extends InjectAdapter<RecyclerView.ViewHolder> {

  private static final int HEADER = 0;
  private static final int ITEM = 1;
  private static final int LOADING = 2;

  private boolean isLoadingAdded = false;
  private boolean isLastPageReached = false;
  private final List<RiwayatHeader> mData;
  private final Map<String, Long> headerTotalMap = new HashMap<>();
  public final Activity context;
  private final PaginationAdapterCallback mCallback;

  private OnHistoryClickListener listener;

  public interface OnHistoryClickListener {
    void onClick(ResponseHistory.Data data);
  }

  public void setOnHistoryClickListener(OnHistoryClickListener listener) {
    this.listener = listener;
  }

  public HistoryPagingAdapter(Activity context) {
    this.context = context;
    this.mData = new ArrayList<>();
    this.mCallback =
        context instanceof PaginationAdapterCallback ? (PaginationAdapterCallback) context : null;
  }

  public void setLastPage(boolean isLastPage) {
    this.isLastPageReached = isLastPage;
  }

  public List<RiwayatHeader> getData() {
    return mData;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    switch (viewType) {
      case HEADER:
        return new HeaderViewHolder(ItemHeaderBinding.inflate(inflater, parent, false));
      case ITEM:
        return new MyViewHolder(ItemStatusTransaksiBinding.inflate(inflater, parent, false));
      case LOADING:
        return new LoadingViewHolder(ItemFooterLoadingBinding.inflate(inflater, parent, false));
      default:
        throw new IllegalArgumentException("Unknown view type: " + viewType);
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    switch (getItemViewType(position)) {
      case HEADER:
        RiwayatHeader header = mData.get(position);
        String headerText = header.getHeaderDate();
        long total = headerTotalMap.getOrDefault(headerText, 0L);
        if (header.getHeaderDate().equalsIgnoreCase(AndroidTimes.getTodayFormatted())) {
          headerText = "Hari Ini";
        }
        ((HeaderViewHolder) holder).bind(headerText, total);
        break;
      case ITEM:
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        boolean isLastInSection =
            position + 1 >= mData.size()
                || mData.get(position + 1).getType() == RiwayatHeader.TYPE_HEADER;
        myViewHolder.bind(context, mData.get(position).getData(), isLastInSection);
        myViewHolder.binding.root.setOnClickListener(
            v -> {
              if (listener != null) {
                listener.onClick(mData.get(position).getData());
              }
            });
        break;
      case LOADING:
        ((LoadingViewHolder) holder).bind(!isLastPageReached);
        break;
    }
  }

  public void updateStatus(String refId, String newStatus) {
    for (int i = 0; i < mData.size(); i++) {
      RiwayatHeader headerItem = mData.get(i);
      if (headerItem.getType() == RiwayatHeader.TYPE_ITEM) {
        ResponseHistory.Data item = headerItem.getData();
        if (item != null && item.getRefId().equals(refId)) {
          item.setStatus(newStatus);
          notifyItemChanged(i); // hanya update item itu saja
          break;
        }
      }
    }
  }

  public void addAll(List<ResponseHistory.Data> data) {
    String lastHeader = null;
    for (int i = mData.size() - 1; i >= 0; i--) {
      if (mData.get(i).getType() == RiwayatHeader.TYPE_HEADER) {
        lastHeader = mData.get(i).getHeaderDate();
        break;
      }
    }

    List<RiwayatHeader> result = new ArrayList<>();
    String currentHeader = lastHeader;
    RiwayatHeader headerHolder = null;

    for (ResponseHistory.Data item : data) {
      String fullTanggal = item.getTanggal().trim();
      String[] splitTanggal = fullTanggal.split(" - ");
      String headerDate = splitTanggal.length > 0 ? splitTanggal[0] : fullTanggal;

      boolean isNewHeader = !headerDate.equals(currentHeader);
      if (isNewHeader && !containsHeader(headerDate)) {
        headerHolder = new RiwayatHeader(headerDate);
        result.add(headerHolder);
        currentHeader = headerDate;
      }

      result.add(new RiwayatHeader(item));

      if ("sukses".equalsIgnoreCase(item.getStatus())) {
        long total = headerTotalMap.containsKey(headerDate) ? headerTotalMap.get(headerDate) : 0;
        headerTotalMap.put(headerDate, total + item.getJumlah());
      }
    }

    // Hapus header duplikat jika perlu
    if (!result.isEmpty() && lastHeader != null) {
      RiwayatHeader first = result.get(0);
      if (first.getType() == RiwayatHeader.TYPE_HEADER
          && lastHeader.equals(first.getHeaderDate())) {
        result.remove(0);
      }
    }

    int start = mData.size();
    mData.addAll(result);
    notifyItemRangeInserted(start, result.size());
  }

  private boolean containsHeader(String headerDate) {
    for (RiwayatHeader item : mData) {
      if (item.getType() == RiwayatHeader.TYPE_HEADER && item.getHeaderDate().equals(headerDate)) {
        return true;
      }
    }
    return false;
  }

  public void clear() {
    mData.clear();
    headerTotalMap.clear();
    isLoadingAdded = false;
    isLastPageReached = false;
    notifyDataSetChanged();
  }

  public boolean isEmpty() {
    return getItemCount() == 0;
  }

  public void addLoadingFooter() {
    if (!isLoadingAdded) {
      isLoadingAdded = true;
      notifyItemInserted(mData.size());
    }
  }

  public void removeLoadingFooter() {
    if (isLoadingAdded) {
      int position = getItemCount() - 1;
      isLoadingAdded = false;
      notifyItemRemoved(position);
    }
  }

  @Override
  public int getItemCount() {
    int itemCount = mData.size();

    // Jangan tampilkan footer jika item < 10
    if ((isLoadingAdded || isLastPageReached) && itemCount >= 10) {
      return itemCount + 1;
    } else {
      return itemCount;
    }
  }

  @Override
  public int getItemViewType(int position) {
    if ((isLoadingAdded || isLastPageReached) && position == mData.size()) {
      return LOADING;
    }
    return mData.get(position).getType();
  }

  public static class HeaderViewHolder extends RecyclerView.ViewHolder {
    ItemHeaderBinding binding;

    public HeaderViewHolder(ItemHeaderBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(String date, long total) {
      binding.tvHeader.setText(date);
      binding.infoPrice.setText("-" + FormatUtils.formatRupiah(total));
    }
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {
    ItemStatusTransaksiBinding binding;

    public MyViewHolder(ItemStatusTransaksiBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(Activity activity, ResponseHistory.Data model, boolean isLastInSection) {
      binding.price.setText(FormatUtils.formatRupiah(model.getJumlah()));
      binding.tvDate.setText(model.getTanggal());
      binding.customerId.setText(model.getKeterangan());

      String namaProduk = model.getJenis();
      if ("Data".equalsIgnoreCase(namaProduk)) {
        namaProduk = "Paket Data";
        binding.tvProduk.setText(namaProduk);
      } else if ("E-Money".equalsIgnoreCase(namaProduk)) {
        namaProduk = "E-Wallet";
        binding.tvProduk.setText(namaProduk);
      } else {
        binding.tvProduk.setText(namaProduk);
      }

      String kategori = model.getKategori();
      Glide.with(binding.brandIcon).load(model.getBrandIconUrl()).into(binding.brandIcon);

      binding.divider.setVisibility(isLastInSection ? View.GONE : View.VISIBLE);

      AdapterHelper.setStatusColor(binding.tvStatus, model.getStatus(), binding.root.getContext());
    }
  }

  public static class LoadingViewHolder extends RecyclerView.ViewHolder {
    ItemFooterLoadingBinding binding;

    public LoadingViewHolder(ItemFooterLoadingBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(boolean loading) {
      if (loading) {
        // binding.footerText.setText("Memuat Data....");
        binding.footerText.setVisibility(View.GONE);
        binding.progress.setVisibility(View.VISIBLE);
      } else {
        binding.footerText.setVisibility(View.VISIBLE);
        binding.footerText.setText("Kamu Sudah Paling Jauh :')");
        binding.progress.setVisibility(View.GONE);
      }
    }
  }
}

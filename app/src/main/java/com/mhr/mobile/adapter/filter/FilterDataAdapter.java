package com.mhr.mobile.adapter.filter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.filter.FilterDataAdapter.FilterVH;
import com.mhr.mobile.api.response.ResponsePricelist;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterDataAdapter extends RecyclerView.Adapter<FilterDataAdapter.FilterVH> {
  private List<ResponsePricelist> allData; // Semua data
  private int selectedPosition = -1;
  private OnFilterSelectedListener listener; // Callback untuk mengirimkan filter
  private boolean isMasaAktifOrKuota;

  public interface OnFilterSelectedListener {
    void onFilterSelected(ResponsePricelist filter);
  }

  public void setOnFilterSelected(OnFilterSelectedListener listener) {
    this.listener = listener;
  }

  public FilterDataAdapter(List<ResponsePricelist> allData, boolean isMasaAktifOrKuota) {
    this.allData = removeDuplicates(allData); // Salin data
    this.isMasaAktifOrKuota = isMasaAktifOrKuota;
  }

  @NonNull
  @Override
  public FilterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.item_filter_data, parent, false);
    return new FilterVH(view);
  }

  @Override
  public void onBindViewHolder(@NonNull FilterVH holder, int position) {
    ResponsePricelist filter = allData.get(position); // Ambil item dari semua data

    String produkDetail = filter.getProductName();
    if (produkDetail == null || produkDetail.trim().equals("/")) return;

    if (isMasaAktifOrKuota) {
      holder.button.setText(filter.getMasaAktif());
    } else {
      holder.button.setText(filter.getJumlahKuota());
    }

    // Tandai item yang dipilih
    if (selectedPosition == position) {
      holder.button.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#000000")));
    } else {
      holder.button.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
    }

    // Event klik untuk memilih filter
    holder.button.setOnClickListener(
        v -> {
          updateSelectedPosition(position); // Tetapkan filter yang dipilih
          // Kirimkan filter yang dipilih melalui listener
          if (listener != null) {
            listener.onFilterSelected(filter);
          }
        });
  }

  public String extractHari(String input) {
    Pattern pattern = Pattern.compile("(\\d+)\\s*hari");
    Matcher matcher = pattern.matcher(input.toLowerCase());

    if (matcher.find()) {
      return matcher.group(); // Mengembalikan "1 hari", "7 hari", dll
    }
    return "";
  }

  public String extractKuota(String productNominal) {
    // Cek apakah productNominal kosong atau hanya "-"
    if (productNominal == null || productNominal.trim().equals("-")) {
      return ""; // Mengembalikan string kosong jika tidak valid
    }

    // Menghapus simbol atau karakter lain yang bukan angka, koma, titik, atau unit GB/MB
    String cleanedNominal = productNominal.replaceAll("[^\\d,\\.\\sGBMB]", "").trim();

    // Regex untuk mencocokkan angka desimal (baik koma atau titik) yang diikuti oleh unit GB atau
    // MB
    Pattern pattern =
        Pattern.compile("(\\d+[,.]?\\d*)\\s*(GB|MB)"); // Menangkap angka desimal dan unit
    Matcher matcher = pattern.matcher(cleanedNominal);

    if (matcher.find()) {
      // Gabungkan angka dan unit tanpa spasi di antara keduanya
      return matcher.group(1).replace(",", ".")
          + matcher.group(2); // Mengganti koma dengan titik untuk format desimal
    }
    return ""; // Jika tidak ada kecocokan, kembalikan string kosong
  }

  @Override
  public int getItemCount() {
    return allData.size();
  }

  public void updateDataMasaAktif(List<ResponsePricelist> productList) {
    this.allData = new ArrayList<>(removeDuplicates(productList));

    // Urutkan berdasarkan angka hari dari nama produk
    Collections.sort(
        allData,
        new Comparator<ResponsePricelist>() {
          @Override
          public int compare(ResponsePricelist o1, ResponsePricelist o2) {
            int hari1 = extractAngkaHari(o1.getMasaAktif());
            int hari2 = extractAngkaHari(o2.getMasaAktif());
            return Integer.compare(hari1, hari2);
          }
        });

    notifyDataSetChanged();
  }

  private int extractAngkaHari(String input) {
    Pattern pattern = Pattern.compile("(\\d+)\\s*hari");
    Matcher matcher = pattern.matcher(input.toLowerCase());

    if (matcher.find()) {
      return Integer.parseInt(matcher.group(1));
    }
    return Integer.MAX_VALUE; // Supaya item tanpa "hari" ditaruh di akhir
  }

  private List<ResponsePricelist> removeDuplicates(List<ResponsePricelist> productList) {
    Map<String, ResponsePricelist> uniqueProducts = new HashMap<>();

    for (ResponsePricelist product : productList) {
      String key =
          isMasaAktifOrKuota
              ? extractHari(product.getMasaAktif()).toLowerCase().trim()
              : extractKuota(product.getMasaAktif()).toLowerCase().trim();
      // Jika belum ada key ini, simpan
      if (!uniqueProducts.containsKey(key) && !key.isEmpty()) {
        uniqueProducts.put(key, product);
      }
    }

    return new ArrayList<>(uniqueProducts.values());
  }

  public void sortByActivePeriod() {
    // Langkah 1: Urutkan data berdasarkan ActivePeriod
    Collections.sort(
        allData,
        new Comparator<ResponsePricelist>() {
          @Override
          public int compare(ResponsePricelist o1, ResponsePricelist o2) {
            try {
              // Mengonversi getActivePeriod() dari string ke integer
              int activePeriod1 = Integer.parseInt(o1.getProductName().trim()); // Ambil angka
              int activePeriod2 = Integer.parseInt(o2.getProductName().trim()); // Ambil angka

              return Integer.compare(activePeriod1, activePeriod2); // Bandingkan angka
            } catch (NumberFormatException e) {
              // Jika terjadi error (misalnya getActivePeriod() tidak bisa dikonversi ke integer)
              return 0; // Bisa diganti dengan logika lain jika diperlukan
            }
          }
        });

    // Langkah 2: Hapus angka "0" jika berada di posisi pertama setelah pengurutan
    if (!allData.isEmpty() && allData.get(0).getProductName().equals("0")) {
      allData.remove(0); // Hapus angka "0" dari posisi pertama
    }

    // Setelah pengurutan dan pembersihan angka "0", refresh adapter
    notifyDataSetChanged();
  }

  public static class FilterVH extends RecyclerView.ViewHolder {
    MaterialButton button;

    public FilterVH(View itemView) {
      super(itemView);
      button = itemView.findViewById(R.id.btnMasaAktif);
    }
  }

  public void updateSelectedPosition(int newSelectedPosition) {
    int previousPosition = selectedPosition;
    selectedPosition = newSelectedPosition;
    // Refresh tampilan item yang berubah
    notifyItemChanged(previousPosition);
    notifyItemChanged(newSelectedPosition);
  }
}

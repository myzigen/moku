package com.mhr.mobile.ui.produk.helper;

import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.util.StrUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperFilterKuota {

  // * Filter berdasarkan jumlah kuota
  public static boolean filterByKuota(ResponsePricelist product, String kuota) {
    String productDetails = product.getProductName(); // Ambil detail produk
    double productKuota = extractKuotaFromDetails(productDetails); // Kuota dalam satuan GB atau MB

    if (productKuota <= 0) return false;

    switch (kuota) {
      case "< 1GB":
        // Tampilkan produk dengan kuota kurang dari atau sama dengan 1GB
        return productKuota <= 1; // Kuota <= 1GB (termasuk 1GB)
      case "1 - 5GB":
        return productKuota >= 1 && productKuota <= 5; // Kuota antara 1GB hingga 10GB
      case "6 - 10GB":
        return productKuota > 6 && productKuota <= 10; // Kuota antara 10GB hingga 50GB
      case "11 - 20GB":
        return productKuota > 11 && productKuota <= 20;
      case "> 20GB":
        return productKuota >= 999;
      default:
        return false;
    }
  }

  private static double extractKuotaFromDetails(String details) {
    try {
      // Regex untuk menangkap angka dan unit (MB atau GB)
      Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s*(MB|GB)", Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(details);

      if (matcher.find()) {
        double value = Double.parseDouble(matcher.group(1)); // Ambil angka kuota
        String unit = matcher.group(3).toUpperCase(); // Ambil satuan (MB atau GB)

        if (unit.equals("MB")) {
          return value / 1024; // Konversi MB ke GB (jika perlu untuk perbandingan)
        } else if (unit.equals("GB")) {
          return value; // Sudah dalam GB
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0; // Jika parsing gagal
  }

  public static int urutkanHari(String input) {
    Pattern pattern = Pattern.compile("(\\d+)\\s*hari");
    Matcher matcher = pattern.matcher(input.toLowerCase());

    if (matcher.find()) {
      return Integer.parseInt(matcher.group(1));
    }
    return Integer.MAX_VALUE; // Supaya item tanpa "hari" ditaruh di akhir
  }

  public static boolean noFilterSelected() {
    return StrUtils.FILTER_PAKET.isEmpty()
        && StrUtils.FILTER_KUOTA.isEmpty()
        && StrUtils.FILTER_MASA_AKTIF.isEmpty();
  }
}

package com.mhr.mobile.ui.produk.helper;

import com.mhr.mobile.api.response.ResponsePricelist;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HelperFilterProduk {

  private static final List<String> KUOTA_BRANDS =
      Arrays.asList("TELKOMSEL", "INDOSAT", "AXIS", "BY.U", "TRI", "XL", "SMARTFREN");

  public static List<ResponsePricelist> filterVoucherBelanja(List<ResponsePricelist> products) {
    List<ResponsePricelist> filtered = new ArrayList<>();
    for (ResponsePricelist item : products) {
      if (!KUOTA_BRANDS.contains(item.getBrand().toUpperCase())) {
        filtered.add(item);
      }
    }
    return filtered;
  }

  public static List<ResponsePricelist> filterVoucherKuota(List<ResponsePricelist> products) {
    List<ResponsePricelist> filtered = new ArrayList<>();
    for (ResponsePricelist item : products) {
      if (KUOTA_BRANDS.contains(item.getBrand().toUpperCase())) {
        filtered.add(item);
      }
    }
    return filtered;
  }

  public static List<ResponsePricelist> getFilterProvider(
      List<ResponsePricelist> data, String provider) {
    return data.stream()
        .filter(produk -> produk.getBrand().equalsIgnoreCase(provider))
        .collect(Collectors.toList());
  }
}

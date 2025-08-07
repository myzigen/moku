package com.mhr.mobile.ui.produk.prepaid.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.inquiry.request.InquiryGames;
import com.mhr.mobile.ui.produk.abs.AbsPrepaidReload;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.preferences.PrefInputCache;
import java.util.ArrayList;
import java.util.List;

public class GameReload extends AbsPrepaidReload {
  private boolean idGameSudahDicek = false;
  private String cachedNomor = null;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    View v = super.onCreateQiosView(layoutInflater, viewGroup, bundle);
    absFilterVisibility(tidak);
    loadCache();
    return v;
  }

  private void checkIdGames(String gamename, String id, ResponsePricelist model) {
    InquiryGames.request(
        this,
        gamename,
        id,
        "",
        new InquiryGames.GameCallback() {
          @Override
          public void onStart() {
            binding.pembayaran.btnLanjutBayar.setEnabled(false);
            binding.progressExpand.setVisibility(View.VISIBLE);
            showExpandLoading("Pengecekan ID Game");
          }

          @Override
          public void onSuccess(String nickname) {
            inquiry = nickname;
            showExpandSuccess(nickname);
            infoPembelian(model, id);
            saveCache();
            binding.progressExpand.setVisibility(View.GONE);
            binding.pembayaran.btnLanjutBayar.setEnabled(true);
          }

          @Override
          public void onError(String message) {
            showExpandError(message);
            binding.progressExpand.setVisibility(View.GONE);
            binding.pembayaran.btnLanjutBayar.setEnabled(false);
          }
        });
  }

  @Override
  protected String absTitleToolbar() {
    return getIntent().getStringExtra("brand");
  }

  @Override
  protected String absText() {
    return "ID Game";
  }

  @Override
  protected String absPlaceholderText() {
    return "1234567890";
  }

  @Override
  protected void absAfterTextChanged(String nomor) {
    if (nomor.isEmpty()) {
      hideExpandError();
      reset();
      return;
    }
    // Jika sama dengan cache, langsung tampilkan
    if (nomor.equalsIgnoreCase(cachedNomor) && inquiry != null && !inquiry.isEmpty()) {
      showExpandSuccess(inquiry);
      idGameSudahDicek = true;
    } else if (!nomor.equalsIgnoreCase(cachedNomor)) {
      idGameSudahDicek = false; // supaya nanti inquiry dipanggil ulang
      inquiry = null; // reset nama biar gak salah tampilin
      binding.pembayaran.btnLanjutBayar.setEnabled(true);
    }
    hideExpandError();
  }

  private void reset() {
    adapter.setInputValid(tidak);
    adapter.resetSelectedPosition();
    binding.wrapperPembayaran.setVisibility(View.GONE);
  }

  @Override
  protected void absValidate(ResponsePricelist pricelist) {
    String validate = editText.getText().toString();
    String brand = getAbsIntent("brand");
    if (validate.isEmpty()) {
      showExpandError("ID Game " + brand + " Belum Di Isi");
      adapter.setInputValid(false);
      adapter.resetSelectedPosition();
      return;
    }

    if (validate.length() < 5) {
      showExpandError("Nomor tidak sah");
      adapter.setInputValid(false);
      adapter.resetSelectedPosition();
      return;
    }

    hideKeyboard();
    adapter.setInputValid(true);
    absDetectProvider(validate);
    binding.wrapperPembayaran.setVisibility(View.VISIBLE);
    binding.pembayaran.harga.setText(FormatUtils.formatRupiah(pricelist.getHargaJual()));

    // ✅ Cek jika data valid & cocok dengan cache
    if (validate.equalsIgnoreCase(cachedNomor) && inquiry != null && !inquiry.isEmpty()) {
      showExpandSuccess(inquiry);
      infoPembelian(pricelist, validate);
      idGameSudahDicek = true;
      return; // ✅ tidak usah panggil ulang
    }

    // ❌ Reset inquiry agar tidak salah tampilkan nama
    inquiry = null;
    idGameSudahDicek = false;

    // 🔄 Kalau belum cache atau berubah → lakukan pengecekan API
    checkIdGames(formatGameApi(brand), validate, pricelist);
    idGameSudahDicek = true;
  }

  private String formatGameApi(String namaGame) {
    return namaGame.toLowerCase().trim().replace(" ", "_");
  }

  @Override
  protected void absDetectProvider(String nomor) {}

  @Override
  protected void absNomorRiwayat(String nomor) {}

  @Override
  protected String absKategoriProduk() {
    return getAbsIntent("kategori");
  }

  @Override
  protected void absProdukLoaded(List<ResponsePricelist> produk) {
    List<ResponsePricelist> filtered = new ArrayList<>();
    for (ResponsePricelist item : produk) {
      if (item.getBrand().equalsIgnoreCase(getAbsIntent("brand"))) {
        filtered.add(item);
      }
    }
    adapter.perbaruiData(filtered);
  }

  @Override
  protected void absProdukFailed(String failed) {}

  @Override
  protected String absBrandIconUrl() {
    return getAbsIntent("brand_icon");
  }

  private void saveCache() {
    PrefInputCache cache = new PrefInputCache();
    cache.nomor = editText.getText().toString();
    cache.customer_name = inquiry;
    String json = new Gson().toJson(cache);
    pref.setString("cache_games" + getAbsIntent("brand").toLowerCase(), json);
  }

  private void loadCache() {
    String json = pref.getString("cache_games" + getAbsIntent("brand").toLowerCase());
    if (json != null && !json.isEmpty()) {
      PrefInputCache cache = new Gson().fromJson(json, PrefInputCache.class);

      editText.setText(cache.nomor);
      this.inquiry = cache.customer_name;
      this.cachedNomor = cache.nomor;
    }
  }
}

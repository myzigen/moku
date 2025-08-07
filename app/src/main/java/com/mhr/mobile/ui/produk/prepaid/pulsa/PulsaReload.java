package com.mhr.mobile.ui.produk.prepaid.pulsa;

import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.mhr.mobile.R;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.ui.produk.abs.AbsPrepaidReload;
import com.mhr.mobile.ui.produk.helper.HelperFilterProduk;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.ContactUtils;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.ProviderUtils;
import java.util.ArrayList;
import java.util.List;

public class PulsaReload extends AbsPrepaidReload {
  private ArrayList<String> kontakList = new ArrayList<>();
  private ArrayList<String> kontakFiltered = new ArrayList<>();
  private ArrayAdapter<String> arrayAdapter;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    View view = super.onCreateQiosView(layoutInflater, viewGroup, bundle);
    absFilterVisibility(tidak);
    viewKontak();
    //riwayatLastNumber(absKategoriProduk());
    return view;
  }

  private void viewKontak() {
    arrayAdapter =
        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, kontakFiltered) {
          @Override
          public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setText(Html.fromHtml(getItem(position), Html.FROM_HTML_MODE_LEGACY));
            return view;
          }
        };

    binding.listKontak.setAdapter(arrayAdapter);

    binding.listKontak.setOnItemClickListener((parent, view, position, id) -> {
      // ✅ safe: cek posisi valid
      if (position >= 0 && position < kontakFiltered.size()) {
        String raw = Html.fromHtml(kontakFiltered.get(position), Html.FROM_HTML_MODE_LEGACY).toString();
        String[] parts = raw.split(":");
        String nomor = (parts.length > 1)
            ? parts[1].trim().replaceAll("[^\\d]", "")
            : raw.trim().replaceAll("[^\\d]", "");
        binding.etPhoneNumber.setText(nomor);
        binding.etPhoneNumber.setSelection(nomor.length());
        binding.listKontak.setVisibility(View.GONE);
        adapter.resetSelectedPosition();
        binding.wrapperPembayaran.setVisibility(View.GONE);
        hideKeyboard();
      }
    });

    cekIzinKontak();
  }

  @Override
  protected String absTitleToolbar() {
    return getString(R.string.pulsa);
  }

  @Override
  protected String absText() {
    return getString(R.string.title_nomor);
  }

  @Override
  protected String absPlaceholderText() {
    return getString(R.string.initial_nomor_hp);
  }

  @Override
  protected void absNomorRiwayat(String nomor) {
    if (nomor == null || nomor.trim().isEmpty()) return;

    // ✅ safe: cek editText tidak null
    if (editText != null) {
      String current = editText.getText().toString().trim();
      if (!nomor.equals(current)) {
        editText.setText(nomor);
        absAfterTextChanged(nomor);
      } else {
        absDetectProvider(nomor);
      }
    }
  }

  @Override
  protected void ambilKontak() {
    mulaiAmbilKontak();
  }

  private void mulaiAmbilKontak() {
    kontakList = ContactUtils.getAllContacts(this);
    kontakFiltered.clear();
    kontakFiltered.addAll(kontakList);
    arrayAdapter.notifyDataSetChanged();
  }

  @Override
  protected void absValidate(ResponsePricelist pricelist) {
    String nomor = editText.getText().toString();
    if (nomor.isEmpty()) {
      showExpandError("Nomor belum di isi");
      return;
    }
    if (nomor.length() < 10) {
      showExpandError("Nomor tidak sah");
      return;
    }

    hideKeyboard();
    adapter.setInputValid(true);
    binding.wrapperPembayaran.setVisibility(View.VISIBLE);
    binding.pembayaran.harga.setText(FormatUtils.formatRupiah(pricelist.getHargaJual()));
    infoPembelian(pricelist, nomor);
  }

  @Override
  protected void absDetectProvider(String nomor) {
    if (allProduk == null || allProduk.isEmpty()) return;

    String detect = ProviderUtils.detectProvider(nomor);
    if (detect.equals("Unknown")) {
      showExpandError("Nomor pelanggan tidak dikenali");
      initVisibility(false);
      return;
    }

    hideExpandError();

    List<ResponsePricelist> filter = HelperFilterProduk.getFilterProvider(allProduk, detect);

    // ✅ safe: cek filter tidak kosong sebelum ambil index 0
    if (!filter.isEmpty()) {
      String iconUrl = filter.get(0).getBrandIconUrl();
      Glide.with(this) // ✅ safe: pakai requireContext()
          .load(iconUrl)
          .placeholder(R.drawable.ic_no_image)
          .error(R.drawable.ic_chip)
          .into(binding.logoProvider);
    }

    notifyAdapter(filter);
  }

  @Override
  protected void onPhoneNumberPicked(String phoneNumber) {
    editText.setText(phoneNumber);
    binding.etPhoneNumber.setSelection(phoneNumber.length());
    absDetectProvider(phoneNumber);
    hideKeyboard();
    initVisibility(true);
  }

  @Override
  protected String absKategoriProduk() {
    return getAbsIntent("pulsa");
  }

  @Override
  protected String absBrandIconUrl() {
    return null;
  }

  @Override
  protected void absProdukLoaded(List<ResponsePricelist> produk) {
    initVisibility(false);
    String nomor = binding.etPhoneNumber.getText().toString().trim();
    if (!nomor.isEmpty() && nomor.length() >= 10 && nomor.startsWith("0")) {
      absDetectProvider(nomor);
    }
  }

  @Override
  protected void absProdukFailed(String failed) {
    initVisibility(false);
    binding.listKontak.setVisibility(View.GONE);
  }

  private void notifyAdapter(List<ResponsePricelist> pricelist) {
    if (pricelist.isEmpty()) {
      initVisibility(false);
    } else {
      initVisibility(true);
      adapter.perbaruiData(pricelist);
    }
  }

  private void initVisibility(boolean visibility) {
    binding.infoNoProduk.setVisibility(visibility ? View.GONE : View.VISIBLE);
    binding.recyclerview.hideShimmerAdapter();
  }

  private void reset() {
    initVisibility(false);
    binding.wrapperPembayaran.setVisibility(View.GONE);
    binding.logoProvider.setImageResource(R.drawable.ic_chip);
    adapter.resetSelectedPosition();
    adapter.setInputValid(false);
    adapter.perbaruiData(new ArrayList<>());
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    String query = s.toString().trim();
    kontakFiltered.clear();

    if (!query.isEmpty()) {
      List<String> hasil = ContactUtils.filterContacts(kontakList, query);
      if (!hasil.isEmpty()) {
        String highlight = ContactUtils.highlightQuery(hasil.get(0), query);
        kontakFiltered.add(highlight);
        binding.listKontak.setVisibility(View.VISIBLE);
      } else {
        binding.listKontak.setVisibility(View.GONE);
      }
    } else {
      binding.listKontak.setVisibility(View.GONE);
    }

    arrayAdapter.notifyDataSetChanged();
    
    // ✅ safe: cek binding.clearText tidak null
    if (binding.clearText != null) {
      binding.clearText.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
    }
  }

  @Override
  protected void absAfterTextChanged(String nomor) {
    if (nomor.isEmpty()) {
      hideExpandError();
      reset();
    } else if (nomor.startsWith("0") && nomor.length() >= 10) {
      absDetectProvider(nomor);
      adapter.resetSelectedPosition();
    } else {
      reset();
    }
  }
}

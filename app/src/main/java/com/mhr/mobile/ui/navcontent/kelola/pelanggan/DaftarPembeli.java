package com.mhr.mobile.ui.navcontent.kelola.pelanggan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.InjectionRecyclerviewBinding;
import com.mhr.mobile.model.RekapNomorModel;
import com.mhr.mobile.ui.inject.InjectionActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DaftarPembeli extends InjectionActivity {
  private InjectionRecyclerviewBinding binding;
  private RekapPembeliAdapter adapter;
  private List<RekapNomorModel> mData = new ArrayList<>();

  @Override
  protected String getTitleToolbar() {
    return "Daftar Pembeli";
  }

  @Override
  public View onCreateQiosView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = InjectionRecyclerviewBinding.inflate(inflater, viewGroup, false);
    rekapData();
    return binding.getRoot();
  }

  private void rekapData() {
    binding.recyclerview.addItemDecoration(getSpacingItemDecoration(1, 50, true));
    binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    adapter = new RekapPembeliAdapter(mData);
    binding.recyclerview.setAdapter(adapter);
    OkHttpClient client = new OkHttpClient();

    // Ganti dengan token yang kamu simpan saat login
    String token = session.getToken();

    // Ganti dengan URL PHP kamu
    String url = "https://api.qiospro.my.id/api/users/users_total_pembelian.php";

    Request request =
        new Request.Builder().url(url).addHeader("Authorization", "Bearer " + token).build();

    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Tambahkan handler untuk gagal jaringan
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                  String responseBody = response.body().string();

                  // Contoh: parsing dengan JSONObject
                  try {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                      JSONArray dataArray = jsonObject.getJSONArray("data");
                      mData.clear();
                      for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject item = dataArray.getJSONObject(i);
                        String customerNo = item.getString("customer_no");
                        int totalPembelian = item.getInt("total_pembelian");
                        mData.add(new RekapNomorModel(customerNo, totalPembelian));

                        // Log.d("Transaksi", customerNo + " - Rp" + totalPembelian);
                      }
                      runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } else {
                      String message = jsonObject.getString("message");
                      // Log.e("API", "Gagal: " + message);
                    }

                  } catch (JSONException e) {
                    e.printStackTrace();
                  }
                } else {
                  // Log.e("API", "Error Response: " + response.code());
                }
              }
            });
  }
}

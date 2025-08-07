package com.mhr.mobile.ui.navcontent.akun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.AkunAdapter;
import com.mhr.mobile.databinding.UserDevicesBinding;
import com.mhr.mobile.model.Akun;
import com.mhr.mobile.ui.inject.InjectionActivity;
import java.util.ArrayList;
import java.util.List;

public class AkunDevices extends InjectionActivity {
  private UserDevicesBinding binding;
  private FirebaseFirestore db;
  private AkunAdapter adapter;

  @Override
  protected String getTitleToolbar() {
    return "Perangkat Terhubung";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserDevicesBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();
    String userId = auth.getUid();
    getPerangkatTertaut(userId);
    binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    adapter = new AkunAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
  }

  private void getPerangkatTertaut(String userId) {
    db.collection("users")
        .document(userId)
        .get()
        .addOnSuccessListener(
            queryDocumentSnapshots -> {
              List<Akun> deviceList = new ArrayList<>();
              String deviceName = queryDocumentSnapshots.getString("userDeviceName");
              deviceList.add(new Akun(R.drawable.itm_bank, deviceName));
              adapter.updateData(deviceList);
            })
        .addOnFailureListener(
            e -> {
              Toast.makeText(this, "Gagal mengambil data perangkat!", Toast.LENGTH_SHORT).show();
            });
  }
}

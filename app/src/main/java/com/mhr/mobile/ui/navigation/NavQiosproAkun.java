package com.mhr.mobile.ui.navigation;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.AkunAdapter;
import com.mhr.mobile.databinding.NavQiosproAkunBinding;
import com.mhr.mobile.model.Akun;
import com.mhr.mobile.ui.MainActivity;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.intro.UserLogin;
import com.mhr.mobile.ui.navcontent.akun.AkunAppSetting;
import com.mhr.mobile.ui.navcontent.akun.AkunDevices;
import com.mhr.mobile.ui.navcontent.akun.AkunMediaPromosi;
import com.mhr.mobile.ui.navcontent.akun.AkunPin;
import com.mhr.mobile.ui.navcontent.akun.AkunProfile;
import com.mhr.mobile.ui.navcontent.akun.AkunToko;
import com.mhr.mobile.ui.navcontent.akun.AkunUpgrade;
import com.mhr.mobile.ui.navcontent.akun.web.WebInjection;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.UtilsManager;
import com.mikelau.shimmerrecyclerviewx.ShimmerRecyclerViewX;
import java.util.ArrayList;
import java.util.List;

public class NavQiosproAkun extends InjectionFragment {
  private NavQiosproAkunBinding binding;
  private ShimmerRecyclerViewX recyclerViewX, recyclerViewX2;
  private AkunAdapter adapter, adapter2;
  private List<Akun> mData = new ArrayList<>();
  private List<Akun> mData2 = new ArrayList<>();
  private boolean isDataLoaded = false;
  private String URL = "https://api.qiospro.my.id/";
  private final ActivityResultLauncher<Intent> tambahPelangganLauncher =
      registerForActivityResult(
          new ActivityResultContracts.StartActivityForResult(),
          result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
              isDataLoaded = false; // pakai flag tadi
              userAkun(); // reload data karena ada data baru
            }
          });

  @Override
  public View onCreateQiosFragment(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = NavQiosproAkunBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    recyclerViewX = binding.recyclerview;
    recyclerViewX2 = binding.recyclerview2;
    binding.tvVersion.setText("Version V" + UtilsManager.getVersionApp(requireActivity()));
    binding.btnTheme.setOnClickListener(
        v -> getMainActivity().targetActivity(AkunAppSetting.class));
    binding.wrapDownline.setOnClickListener(
        v -> {
          MainActivity activity = (MainActivity) getActivity();
          if (activity != null) {
            activity.getCurrentDownline();
          }
        });
    applyRecycler();
    userAkun();
  }

  private void applyRecycler() {
    recyclerViewX.setLayoutManager(getLinearLayoutManager());
    recyclerViewX2.setLayoutManager(getLinearLayoutManager());
    mData.clear();
    mData.add(new Akun(R.drawable.unlock, "Upgrade Akun"));
    mData.add(new Akun(R.drawable.unlock, "Pin Keamanan"));
    mData.add(new Akun(R.drawable.nav_pelanggan_outline, "Atur Toko"));
	mData.add(new Akun(R.drawable.nav_pelanggan_outline, "Media Promosi"));
    mData.add(new Akun(R.drawable.itm_pulsa, "Aktifitas Login"));
    mData.add(new Akun(R.drawable.unlock, "Daftar Harga"));
    mData2.clear();
    mData2.add(new Akun(R.drawable.unlock, "Kendala Dan Masukan"));
    mData2.add(new Akun(R.drawable.ic_ketentuan, "Tentang Aplikasi"));
    mData2.add(new Akun(R.drawable.unlock, "FAQ"));
    mData2.add(new Akun(R.drawable.unlock, "Syarat Ketentuan"));
    mData2.add(new Akun(R.drawable.privacy_policy, "Kebijakan Privasi"));
    mData2.add(new Akun(R.drawable.start_favorite, "Rating Aplikasi"));
    adapter = new AkunAdapter(mData);
    adapter2 = new AkunAdapter(mData2);
    recyclerViewX.setAdapter(adapter);
    recyclerViewX2.setAdapter(adapter2);

    adapter.setOnClickListener(position -> onClickKeamanan(position));
    adapter2.setOnClickListener(position -> onClickInformasi(position));
  }

  private void onClickKeamanan(int position) {
    Intent intent = null;
    switch (position) {
      case 0:
        intent = new Intent(requireActivity(), AkunUpgrade.class);
        break;
      case 1:
        intent = new Intent(requireActivity(), AkunPin.class);
        break;
      case 2:
        intent = new Intent(requireActivity(), AkunToko.class);
        break;
      case 3:
        intent = new Intent(requireActivity(), AkunMediaPromosi.class);
        break;
      case 4:
        intent = new Intent(requireActivity(), WebInjection.class);
        intent.putExtra("url", "https://developer.digiflazz.com/api/buyer/test-case/");
        break;
      case 5:
        intent = new Intent(requireActivity(), WebInjection.class);
        intent.putExtra("url", URL + "digiflazz/ui/syarat_ketentuan.html");
        break;
    }
    if (intent != null) getMainActivity().abStartActivity(intent);
  }

  private void onClickInformasi(int position) {
    Intent intent = null;
    switch (position) {
      case 0:
        intent = new Intent(requireActivity(), AkunPin.class);
        break;
      case 1:
        intent = new Intent(requireActivity(), AkunDevices.class);
        break;
      case 2:
        intent = new Intent(requireActivity(), WebInjection.class);
        intent.putExtra("url", URL + "digiflazz/ui/syarat_ketentuan.html");
        break;
      case 3:
        break;
      case 4:
        intent = new Intent(requireActivity(), WebInjection.class);
        intent.putExtra("url", URL + "ui/kebijakan-privasi.html");
    }
    if (intent != null) getMainActivity().abStartActivity(intent);
  }

  private void userAkun() {
    if (isDataLoaded) return;

    String userName = getSession().getNama();

    if (userName == null) userName = "";
    String nomor = getSession().getNomor();
    String normalizedNo = FormatUtils.denormalizeNomor(nomor);
    String maskingNo = FormatUtils.maskingNomor(normalizedNo);

    int jumlahDownline = getPreferences().getTotalDownline();
    int maxChar = 25;
    String result = userName.length() > maxChar ? userName.substring(0, maxChar) + "..." : userName;
    binding.userDisplayName.setText(result);
    binding.userEmail.setText(maskingNo);
    binding.jumlahDownline.setText(String.valueOf(jumlahDownline));
    binding.initial.setText(FormatUtils.initialName(userName));

    binding.btnUserSetting.setOnClickListener(
        v -> getMainActivity().targetActivity(AkunProfile.class));

    binding.logout.setOnClickListener(
        v -> {
          new AlertDialog.Builder(requireActivity())
              .setTitle("Keluar")
              .setMessage("Apakah anda yakin ingin logout")
              .setPositiveButton(
                  "Ya",
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
					  getPreferences().remove("dark_mode");
                      getSession().logout();
                      getMainActivity().clearActivity(UserLogin.class);
                    }
                  })
              .setNegativeButton("Batal", null)
              .show();
        });
  }

  @Override
  public void onDataReload() {
    super.onDataReload();
  }

  @Override
  public void onResume() {
    super.onResume();
    userAkun();
  }
}

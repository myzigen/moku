package com.mhr.mobile.ui.status;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.LayoutBuktiBayarBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.FormatUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PreviewBuktiBayar extends InjectionActivity {
  private LayoutBuktiBayarBinding binding;

  @Override
  protected String getTitleToolbar() {
    return "Preview";
  }

  @Override
  public View onCreateQiosView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    setTheme(R.style.AppTheme_Light);
    binding = LayoutBuktiBayarBinding.inflate(inflater, viewGroup, false);
	
	String produk = getAbsIntent("produk");
    String brand = getAbsIntent("brand").toUpperCase();
	
    int harga = getIntent().getIntExtra("harga", 0);
	
    binding.infoProduk.setText(produk);
    
    binding.infoId.setText("ID TRANSAKSI " + "#" + getAbsIntent("ref_id"));
    binding.infoTanggal.setText(getAbsIntent("tanggal").toUpperCase());
    binding.infoHp.setText(getAbsIntent("nomor"));

    binding.infoSn.setText(getAbsIntent("sn"));
    binding.btnSave.setOnClickListener(this::saveToGallery);
	binding.infoToko.setText(session.getNamaToko());
	binding.infoAlamatToko.setText(session.getAlamatToko());
    binding.infoBayar.setText(FormatUtils.formatRupiah(harga));
    return binding.getRoot();
  }

  private void saveToGallery(View view) {
    binding.root.post(
        () -> {
          Bitmap receiveBitmap = getBipmapFromView(binding.preview);
          try {
            saveBitmap(receiveBitmap, "id");
          } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show();
          }
        });
  }

  public Bitmap getBipmapFromView(View view) {
    Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    view.draw(canvas);
    return bitmap;
  }

  public void saveBitmap(Bitmap bitmap, String filename) throws IOException {
    OutputStream fos;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      ContentValues values = new ContentValues();
      values.put(MediaStore.Images.Media.DISPLAY_NAME, filename + ".png");
      values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
      values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Receipts");

      // MediaStore to save image
      fos =
          getContentResolver()
              .openOutputStream(
                  getContentResolver()
                      .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values));
    } else {
      File path =
          new File(
              Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
              "Receipts");

      if (!path.exists()) path.mkdirs();

      File imageFile = new File(path, filename + ".png");
      fos = new FileOutputStream(imageFile);
    }

    if (fos != null) {
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
      fos.flush();
      fos.close();

      Toast.makeText(this, "Disimpan ke Galeri", Toast.LENGTH_SHORT).show();
    } else {
      throw new IOException("OutputStream null");
    }
  }
}

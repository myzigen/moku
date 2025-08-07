package com.mhr.mobile.util.bitmap;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;

public class PaletteBitmapResource implements Resource<PaletteBitmap> {
  private final PaletteBitmap paletteBitmap;
  private final BitmapPool bitmapPool;

  public PaletteBitmapResource(
      @NonNull PaletteBitmap paletteBitmap, @NonNull BitmapPool bitmapPool) {
    this.paletteBitmap = paletteBitmap;
    this.bitmapPool = bitmapPool;
  }

  @Override
  public PaletteBitmap get() {
    return paletteBitmap;
  }

  @Override
  public int getSize() {
    return Util.getBitmapByteSize(paletteBitmap.bitmap);
  }

  @Override
  public void recycle() {
    bitmapPool.put(paletteBitmap.bitmap); // Menambahkan kembali bitmap ke dalam pool.
    paletteBitmap.bitmap.recycle(); // Daur ulang bitmap jika tidak diterima oleh pool.
  }

  @Override
  public Class<PaletteBitmap> getResourceClass() {
    return PaletteBitmap.class;
  }
}

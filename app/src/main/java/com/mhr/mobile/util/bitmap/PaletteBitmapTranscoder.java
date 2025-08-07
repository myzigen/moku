package com.mhr.mobile.util.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

public class PaletteBitmapTranscoder implements ResourceTranscoder<Bitmap, PaletteBitmap> {
  private final BitmapPool bitmapPool;

  public PaletteBitmapTranscoder(@NonNull Context context) {
    this.bitmapPool = Glide.get(context).getBitmapPool();
  }

  @Override
  public Resource<PaletteBitmap> transcode(Resource<Bitmap> toTranscode, Options arg1) {
    Bitmap bitmap = toTranscode.get();
    Palette palette = new Palette.Builder(bitmap).generate();
    PaletteBitmap result = new PaletteBitmap(bitmap, palette);
    return new PaletteBitmapResource(result, bitmapPool);
  }
  
  public String getId() {
    return PaletteBitmapTranscoder.class.getName();
  }
}

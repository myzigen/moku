package com.mhr.mobile.util.bitmap;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
public class SaveBitmap {
	
  public static Bitmap getBipmapFromView(View view) {
    Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    view.draw(canvas);
    return bitmap;
  }

  public static void saveBitmap(Context context, Bitmap bitmap, String filename) throws IOException {
    OutputStream fos;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      ContentValues values = new ContentValues();
      values.put(MediaStore.Images.Media.DISPLAY_NAME, filename + ".png");
      values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
      values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Receipts");

      // MediaStore to save image
      fos =
          context.getContentResolver()
              .openOutputStream(
                  context.getContentResolver()
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
    } else {
      throw new IOException("OutputStream null");
    }
  }
}

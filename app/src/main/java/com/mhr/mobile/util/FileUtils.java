package com.mhr.mobile.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {
  public static String getFileFromUri(Context context, Uri uri) {
    try {
      InputStream inputStream = context.getContentResolver().openInputStream(uri);
      String fileName = getFileName(context, uri);
      File file = new File(context.getCacheDir(), fileName);
      FileOutputStream outputStream = new FileOutputStream(file);
      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) > 0) {
        outputStream.write(buffer, 0, length);
      }
      outputStream.close();
      inputStream.close();
      return file.getAbsolutePath();
    } catch (Exception e) {
      return null;
    }
  }

  private static String getFileName(Context context, Uri uri) {
    String name = "temp_file";
    if (uri.getScheme().equals("content")) {
      try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
        if (cursor != null && cursor.moveToFirst()) {
          name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }
      }
    }
    return name;
  }
}

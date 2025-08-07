package com.mhr.mobile.util;

import android.content.Context;
import java.io.File;

public class DeleteCache {

  public static long getDirSize(File dir) {
    long size = 0;
    if (dir != null && dir.isDirectory()) {
      for (File file : dir.listFiles()) {
        if (file != null) {
          if (file.isDirectory()) size += getDirSize(file);
          else size += file.length();
        }
      }
    }
    return size;
  }

  public static void clearCache(Context context) {
    deleteDir(context.getCacheDir());
    deleteDir(context.getExternalCacheDir());
  }

  public static void deleteCache(Context context) {
    try {
      File dir = context.getCacheDir();
      if (dir != null && dir.isDirectory()) {
        deleteDir(dir);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static boolean deleteDir(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (String child : children) {
        boolean success = deleteDir(new File(dir, child));
        if (!success) {
          return false;
        }
      }
      return dir.delete();
    } else if (dir != null && dir.isFile()) {
      return dir.delete();
    }
    return false;
  }
}

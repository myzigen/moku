package com.mhr.mobile.manage.worker;
/*
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.File;
*/
public class CacheCleanupWorker/* extends Worker*/ {
  /*
  public CacheCleanupWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {
    clearCache();
    return Result.success();
  }

  private void clearCache() {
    try {
      File cacheDir = getApplicationContext().getCacheDir();
      if (cacheDir != null && cacheDir.isDirectory()) {
        deleteDir(cacheDir);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean deleteDir(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (String child : children) {
        boolean success = deleteDir(new File(dir, child));
        if (!success) {
          return false;
        }
      }
    }
    return dir.delete();
  }
  */
}

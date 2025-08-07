package com.mhr.mobile.model;

import android.net.Uri;

public class Gallery {
  private Uri imageUri;

  public Gallery(Uri imageUri) {
    this.imageUri = imageUri;
  }

  public Uri getImageUri() {
    return imageUri;
  }
}

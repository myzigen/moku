package com.mhr.mobile.adapter.base;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MediaAdapter<VH extends RecyclerView.ViewHolder, I>
    extends RecyclerView.Adapter<VH> {
  protected Context context;

  protected MediaAdapter(Context context) {
    this.context = context;
  }
}

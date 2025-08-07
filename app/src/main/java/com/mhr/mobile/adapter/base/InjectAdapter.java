package com.mhr.mobile.adapter.base;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

public abstract class InjectAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  public Context context;

  public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

  public abstract void onBindViewHolder(VH holder, int position);

  public abstract int getItemCount();
  

  // Tambahkan metode abstrak lain jika diperlukan
  // public abstract void setData(Object data); // Contoh metode tambahan
}

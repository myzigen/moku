package com.mhr.mobile.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mhr.mobile.databinding.ItemGalleryBinding;
import com.mhr.mobile.model.Gallery;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
  private final List<Gallery> imageList;
  private final Context context;

  public GalleryAdapter(Context context, List<Gallery> imageList) {
    this.context = context;
    this.imageList = imageList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemGalleryBinding binding =
        ItemGalleryBinding.inflate(LayoutInflater.from(context), parent, false);
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Uri imageUri = imageList.get(position).getImageUri();
    Glide.with(context).load(imageUri).into(holder.binding.imageView);
  }

  @Override
  public int getItemCount() {
    return imageList.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private final ItemGalleryBinding binding;

    public ViewHolder(ItemGalleryBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

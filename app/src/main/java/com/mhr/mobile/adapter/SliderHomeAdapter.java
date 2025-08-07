package com.mhr.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mhr.mobile.R;
import com.mhr.mobile.model.ItemSliderHome;
import java.util.List;

public class SliderHomeAdapter extends RecyclerView.Adapter<SliderHomeAdapter.SliderViewHolder> {

  private List<ItemSliderHome> imageList; // Daftar resource gambar
  private Context context;

  public SliderHomeAdapter(List<ItemSliderHome> imageList, Context context) {
    this.imageList = imageList;
    this.context = context;
  }

  @NonNull
  @Override
  public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_slider_home, parent, false);
    return new SliderViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
    ItemSliderHome model = imageList.get(position);

    Glide.with(context)
        .load(model.getSliderImage())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(holder.imageView);
  }

  public void perbaruiData(List<ItemSliderHome> newData) {
    imageList.clear();
    imageList.addAll(newData);
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return imageList.size();
  }

  public static class SliderViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public SliderViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.imageView);
    }
  }
}

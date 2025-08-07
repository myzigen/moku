package com.mhr.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.ItemQiosKategoriBinding;
import com.mhr.mobile.model.MenuKategoriModel;
import com.mhr.mobile.util.QiosColor;
import java.util.List;

public class QiosMenuAdapter extends RecyclerView.Adapter<QiosMenuAdapter.KategoriVH> {

  private Context ctx;
  private List<MenuKategoriModel> mData;

  private OnItemClickListener onItemClickListener;

  public interface OnItemClickListener {
    void onItemClick(int position);
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public QiosMenuAdapter(Context context, List<MenuKategoriModel> list) {
    this.ctx = context;
    this.mData = list;
  }

  @Override
  public KategoriVH onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new KategoriVH(ItemQiosKategoriBinding.inflate(inflater,parent,false));
  }

  @Override
  public void onBindViewHolder(KategoriVH holder, int position) {
    MenuKategoriModel model = mData.get(position);
    String imgUrl = model.getImageUrl();

    // holder.card.setCardBackgroundColor(color);
    if (imgUrl != null && !imgUrl.isEmpty()) {
      QiosColor.applyDominantColorGradient(ctx, imgUrl, holder.ch);
      Glide.with(holder.itemView)
          .load(imgUrl)
          .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
		  .centerCrop()
          .placeholder(R.drawable.kategori_bg)
		  .error(R.drawable.ic_no_image)
          .into(holder.image);
    } else {
      holder.image.setImageResource(model.getIconKategori());
    }

    holder.text.setText(model.getNamaKategori());

    holder.itemView.setOnClickListener(
        v -> {
          if (onItemClickListener != null) {
            onItemClickListener.onItemClick(position);
          }
        });
  }

  // Method untuk memperbarui data
  public void updateData(List<MenuKategoriModel> newItemList) {
    this.mData = newItemList;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class KategoriVH extends RecyclerView.ViewHolder {
    ImageView image;
    TextView text;
    View itemView, ch;

    public KategoriVH(ItemQiosKategoriBinding binding) {
      super(binding.getRoot());
      itemView = binding.getRoot();
      image = binding.imgIconKategori;
      text = binding.itemTextIcon;
      ch = binding.gradient;
    }
  }
}

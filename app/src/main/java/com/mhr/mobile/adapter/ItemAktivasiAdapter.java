package com.mhr.mobile.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.ItemAktivasiBinding;
import java.util.List;

public class ItemAktivasiAdapter extends RecyclerView.Adapter<ItemAktivasiAdapter.ItemAktivasiVH> {

  private List<ItemAktivasi> mData;
  private OnItemClickAktivasi listener;

  public interface OnItemClickAktivasi {
    void onItemAktivasiListener(ItemAktivasi model);
  }

  public void setOnItemClickItemAktivasiListener(OnItemClickAktivasi listener) {
    this.listener = listener;
  }

  public ItemAktivasiAdapter(List<ItemAktivasi> list) {
    this.mData = list;
  }

  @Override
  public ItemAktivasiVH onCreateViewHolder(ViewGroup parent, int viewType) {
    ItemAktivasiBinding binding =
        ItemAktivasiBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new ItemAktivasiVH(binding);
  }

  @Override
  public void onBindViewHolder(ItemAktivasiVH holder, int position) {
    ItemAktivasi model = mData.get(position);

    Glide.with(holder.itemView)
        .load(model.getImageUrl())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(holder.icon);
    holder.txtLabel.setText(model.getBrand());

    holder.itemView.setOnClickListener(
        v -> {
          if (listener != null) {
            listener.onItemAktivasiListener(model);
          }
        });
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class ItemAktivasiVH extends RecyclerView.ViewHolder {
    ImageView icon;
    TextView txtLabel;
    View itemView;

    public ItemAktivasiVH(ItemAktivasiBinding binding) {
      super(binding.getRoot());

      icon = binding.icon;
      txtLabel = binding.txtLabel;
      itemView = binding.root;
    }
  }

  public static class ItemAktivasi implements Parcelable {
    private String imageUrl;
    private String brand;
    private String code;

    public ItemAktivasi() {}

    public ItemAktivasi(String imageUrl, String brand, String code) {
      this.imageUrl = imageUrl;
      this.brand = brand;
      this.code = code;
    }

    protected ItemAktivasi(Parcel in) {
      imageUrl = in.readString();
      brand = in.readString();
      code = in.readString();
    }

    public static final Creator<ItemAktivasi> CREATOR =
        new Creator<ItemAktivasi>() {
          @Override
          public ItemAktivasi createFromParcel(Parcel in) {
            return new ItemAktivasi(in);
          }

          @Override
          public ItemAktivasi[] newArray(int size) {
            return new ItemAktivasi[size];
          }
        };

    public String getImageUrl() {
      return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }

    public String getBrand() {
      return brand;
    }

    public void setBrand(String brand) {
      this.brand = brand;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(imageUrl);
      dest.writeString(brand);
      dest.writeString(code);
    }
  }
}

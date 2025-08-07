package com.mhr.mobile.adapter.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.adapter.pager.SheetListAdapter.MyViewHolder;
import com.mhr.mobile.databinding.ItemListBinding;
import com.mhr.mobile.model.ListSheet;
import java.util.List;

public class SheetListAdapter extends InjectAdapter<SheetListAdapter.MyViewHolder> {
  private Context context;
  private List<ListSheet> mData;
  
  public SheetListAdapter(List<ListSheet> data){
	  this.mData = data;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    context = parent.getContext();
    ItemListBinding binding = ItemListBinding.inflate(LayoutInflater.from(context), parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
	  ListSheet model = mData.get(position);
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    public ItemListBinding binding;

    public MyViewHolder(ItemListBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

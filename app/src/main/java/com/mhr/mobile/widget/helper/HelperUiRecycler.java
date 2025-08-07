package com.mhr.mobile.widget.helper;

import android.app.Activity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;

public class HelperUiRecycler {
  private final Activity activity;
  private RecyclerView.Adapter<?> adapter;
  private RecyclerView recyclerView;

  public HelperUiRecycler(Activity activity) {
    this.activity = activity;
  }
  
  public static HelperUiRecycler with(Activity activity){
	  return new HelperUiRecycler(activity);
  }

  public HelperUiRecycler HorizontalRecycler(RecyclerView recycler) {
	this.recyclerView = recycler;
    recycler.addItemDecoration(new SpacingItemDecoration(1, 14, true));
    recycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
	return this;
  }
  
  public HelperUiRecycler setAdapter(RecyclerView.Adapter<?> adapter){
	  this.adapter = adapter;
	  recyclerView.setAdapter(adapter);
	  return this;
  }
}

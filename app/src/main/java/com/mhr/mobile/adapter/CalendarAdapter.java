package com.mhr.mobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.CalendarAdapter.MyViewHolder;
import com.mhr.mobile.adapter.base.InjectAdapter;
import com.mhr.mobile.databinding.ItemCalendarBinding;
import java.util.List;

public class CalendarAdapter extends InjectAdapter<CalendarAdapter.MyViewHolder> {
  private final List<String> days;
  private final int selectedDayValue;
  private int selectedPosition = -1;
  private OnItemClickListener listener;

  public interface OnItemClickListener {
    void onItemClick(int position, String dayText);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  public CalendarAdapter(List<String> days, int selectedDayValue) {
    this.days = days;
    this.selectedDayValue = selectedDayValue;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    ItemCalendarBinding binding = ItemCalendarBinding.inflate(inflater, parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    String day = days.get(position);
    holder.binding.itemCalendar.setText(day);

    boolean isSelected = false;
    if (!day.isEmpty()) {
      int dayInt = Integer.parseInt(day);
      isSelected = (dayInt == selectedDayValue);
    }

    holder.binding.itemCalendar.setSelected(isSelected);

    holder.binding.itemCalendar.setOnClickListener(
        v -> {
          if (!day.isEmpty()) {
            int oldPos = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(oldPos);
            notifyItemChanged(position);
            listener.onItemClick(position, day);
          }
        });
  }

  @Override
  public int getItemCount() {
    return days.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemCalendarBinding binding;

    public MyViewHolder(ItemCalendarBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}

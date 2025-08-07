package com.mhr.mobile.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.CalendarAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WidgetKalendar extends LinearLayout {

  private TextView tvMonth;
  private RecyclerView recyclerView;
  private OnDateSelectedListener listener;
  private int selectedDay = -1; // hari yang dipilih
  private int selectedYear = -1;
  private int selectedMonth = -1;

  public WidgetKalendar(Context context) {
    super(context);
    init(context);
  }

  public WidgetKalendar(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    setOrientation(VERTICAL);
    LayoutInflater.from(context).inflate(R.layout.widget_kalendar, this, true);

    tvMonth = findViewById(R.id.tvMonth);
    recyclerView = findViewById(R.id.calendarRecyclerView);

    recyclerView.setLayoutManager(new GridLayoutManager(context, 7));

    Calendar calendar = Calendar.getInstance();
    setMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
  }

  public void setMonth(int year, int month) {
    Calendar cal = Calendar.getInstance();
    cal.set(year, month, 1);

    String title = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal.getTime());
    tvMonth.setText(title);

    List<String> days = generateCalendarDays(cal);
    CalendarAdapter adapter = new CalendarAdapter(days, selectedDay);
    recyclerView.setAdapter(adapter);
    adapter.setOnItemClickListener(
        (position, dayText) -> {
          selectedDay = Integer.parseInt(dayText);
          selectedYear = year;
          selectedMonth = month;
          if (listener != null) {
            listener.onDateSelected(year, month, selectedDay);
          }
        });
  }

  public void setSelectedDate(int year, int month, int day) {
    selectedYear = year;
    selectedMonth = month;
    selectedDay = day;

    // Kalau bulan sedang ditampilkan, langsung tampilkan highlight-nya
    setMonth(year, month);
  }

  private List<String> generateCalendarDays(Calendar calendar) {
    List<String> days = new ArrayList<>();
    int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

    for (int i = 0; i < startDayOfWeek; i++) {
      days.add("");
    }
    for (int i = 1; i <= maxDay; i++) {
      days.add(String.valueOf(i));
    }

    return days;
  }

  public void setOnDateSelectedListener(OnDateSelectedListener listener) {
    this.listener = listener;
  }

  public interface OnDateSelectedListener {
    void onDateSelected(int year, int month, int day);
  }
}

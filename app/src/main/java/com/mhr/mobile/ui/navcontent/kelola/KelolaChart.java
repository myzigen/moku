package com.mhr.mobile.ui.navcontent.kelola;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.mhr.mobile.databinding.KelolaChartBinding;
import com.mhr.mobile.ui.inject.InjectionFragment;
import java.util.ArrayList;

public class KelolaChart extends InjectionFragment {
  private KelolaChartBinding binding;
  BarChart barChart;
  int pengeluaranSendiri = 124000;
  int keuntunganPenjualan = 30400;
  int saldo = 100000;

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = KelolaChartBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    barChart = binding.barChart;
    barChart.setPinchZoom(false);
    barChart.setScaleEnabled(false);
    barChart.getDescription().setEnabled(false);
    barChart.setDoubleTapToZoomEnabled(false);
    tampilkanBarChart();
  }

  private void tampilkanBarChart() {
    ArrayList<BarEntry> entries = new ArrayList<>();
    entries.add(new BarEntry(0, pengeluaranSendiri));
	entries.add(new BarEntry(1, 150000));
    entries.add(new BarEntry(2, 165400));
    entries.add(new BarEntry(3, keuntunganPenjualan));
    entries.add(new BarEntry(4, saldo));

    BarDataSet dataSet = new BarDataSet(entries, "Data Transaksi");
    dataSet.setColors(
        new int[] {
          Color.parseColor("#D62F57"),
		  Color.parseColor("#D62F57"),
          Color.parseColor("#21BCBF"),
          Color.parseColor("#21BF73"),
          Color.parseColor("#005FE2")
        });
    dataSet.setValueTextSize(12f);
    dataSet.setValueTextColor(Color.BLACK);
    dataSet.setValueFormatter(
        new ValueFormatter() {
          @Override
          public String getBarLabel(BarEntry barEntry) {
            return "Rp" + (int) barEntry.getY(); // Optional: Tambahkan format
          }
        });

    BarData data = new BarData(dataSet);
    barChart.setData(data);
    barChart.setExtraBottomOffset(16f); // Tambah jarak dari label ke teks bar

    String[] labels = {"Pribadi", "Pengeluaran", "Penjualan", "Keuntungan", "Modal"};
    XAxis xAxis = barChart.getXAxis();
    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
    xAxis.setGranularity(1f);
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setDrawGridLines(false);

    YAxis leftAxis = barChart.getAxisLeft();
    YAxis rightAxis = barChart.getAxisRight();
    rightAxis.setEnabled(false); // Optional: hide right y-axis

    barChart.animateY(1000);
    barChart.invalidate();
  }
}

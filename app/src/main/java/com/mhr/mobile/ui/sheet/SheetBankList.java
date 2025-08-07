package com.mhr.mobile.ui.sheet;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mhr.mobile.adapter.DuitkuAdapter;
import com.mhr.mobile.api.request.duitku.DuitkuRequest;
import com.mhr.mobile.api.request.duitku.DuitkuResponse;
import com.mhr.mobile.databinding.SheetRecyclerviewBinding;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.ui.intro.UserSession;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class SheetBankList extends InjectionSheetFragment {
  private SheetRecyclerviewBinding binding;
  private DuitkuAdapter adapter;
  private OnSheetClickListener listener;

  // Response Checkout
  private String paymentMethod;
  private String customerName;
  private String customerEmail;

  public interface OnSheetClickListener {
    void onSheetClick(String name, String method, String image, String totalFee);
  }

  public void setOnSheetClickListener(OnSheetClickListener listener) {
    this.listener = listener;
  }

  @Override
  protected String getSheetTitle() {
    return "Metode Pembayaran";
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetRecyclerviewBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    initialize();
    applyRecycler();
    loadDataBank();
  }

  private void initialize() {}

  private void applyRecycler() {
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(1, 30, true));
    binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireActivity()));
    adapter = new DuitkuAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnBankClickListener(
        (name, method, image, fee) -> {
          listener.onSheetClick(name, method, image, fee);
          paymentMethod = method;
          dismiss();
        });
  }

  private void loadDataBank() {
    UserSession session = new UserSession(requireActivity());
    DuitkuRequest request = new DuitkuRequest(requireActivity());
    request.setAmount(10000);
    request.requestPayment(
        new DuitkuRequest.Callback() {
          @Override
          public void onPaymentRequest() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onPaymentMethod(List<DuitkuResponse.PaymentMethod> method) {
            adapter.setData(method);
            binding.recyclerview.hideShimmerAdapter();
          }

          @Override
          public void onPaymentError(String error) {
            binding.recyclerview.hideShimmerAdapter();
          }
        });
  }

  @Override
  public void onStart() {
    super.onStart();
    View view = getView();
    if (view != null) {
      View parent = (View) view.getParent();
      BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(parent);

      // Set tinggi maksimum, misalnya 500dp
      int maxHeight =
          (int)
              TypedValue.applyDimension(
                  TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
      parent.getLayoutParams().height = maxHeight;
      parent.requestLayout();

      // Biar tidak expand full saat geser ke atas
      behavior.setPeekHeight(maxHeight);
      // behavior.setHideable(false); // opsional
      behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
  }
}

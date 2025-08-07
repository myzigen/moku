package com.mhr.mobile.ui.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.LoadingDialogBinding;

public class LoadingDialogFragment extends DialogFragment {
  private LoadingDialogBinding binding;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    binding = LoadingDialogBinding.inflate(getLayoutInflater());
    // Menghapus header dialog
    if (getDialog() != null && getDialog().getWindow() != null) {
      getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }
    // Inflate layout loading
    View view = binding.getRoot();

    float radius = 10f;
    float scale = getResources().getDisplayMetrics().density;
    float radiusPx = radius * scale;
    binding.loading.setRadius(radiusPx);
    // binding.lottie.setAnimationFromJson("loading.json");
    // view.setBackgroundResource(R.drawable.corners_alpha);
    return view;
  }

  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);
    if (dialog.getWindow() != null) {
      // dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    dialog.setCanceledOnTouchOutside(false);
    dialog.setCancelable(false);
    // Add back button listener
    /*
      dialog.setOnKeyListener(
          new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
              // getAction to make sure this doesn't double fire
              if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                // Your code here
    	  if (isAdded()){
    		  requireActivity().onBackPressed();
    	  }
                return true; // Capture onKey
              }
              return false; // Don't capture
            }
          });
    */
    return dialog;
  }

  public void showSuccess(Runnable onSuccessComplete) {
    if (binding != null) {
        float startRadius = binding.loading.getRadius(); // radius awal (10dp)
        float endRadius = 50f * getResources().getDisplayMetrics().density;

        ValueAnimator animator = ValueAnimator.ofFloat(startRadius, endRadius);
        animator.setDuration(500); // durasi animasi, bisa disesuaikan
        animator.addUpdateListener(animation -> {
            float animatedRadius = (float) animation.getAnimatedValue();
            binding.loading.setRadius(animatedRadius);
        });

        animator.start();

        binding.lottie.setAnimation(R.raw.price_success);
        binding.lottie.loop(false);
        binding.lottie.playAnimation();
        binding.lottie.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    dismiss();
                    if (onSuccessComplete != null) {
                        onSuccessComplete.run();
                    }
                }, 500);
            }
        });
    }
}

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}

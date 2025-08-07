package com.mhr.mobile.widget.input;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import com.mhr.mobile.R;

public class MokuInputLayout extends TextInputLayout {

  private ImageView contactIcon;
  private OnClickListener contactClickListener;

  public MokuInputLayout(@NonNull Context context) {
    super(context);
    init(context);
  }

  public MokuInputLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public MokuInputLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
  post(() -> {
    ViewGroup endIconFrame = findViewById(getResources().getIdentifier("text_input_end_icon_frame", "id", context.getPackageName())
    );

    if (endIconFrame != null) {
      LayoutInflater inflater = LayoutInflater.from(context);
      // Inflate layout ke dalam endIconFrame
      inflater.inflate(R.layout.custom_endicon_inputlayout, endIconFrame, true);

      contactIcon = endIconFrame.findViewById(R.id.contact_icon);
      if (contactIcon != null) {
        contactIcon.setOnClickListener(v -> {
          if (contactClickListener != null) {
            contactClickListener.onClick(v);
          }
        });
      }
    }
  });
}

  public void setOnContactIconClickListener(OnClickListener listener) {
    this.contactClickListener = listener;
  }

  public ImageView getContactIcon() {
    return contactIcon;
  }
}

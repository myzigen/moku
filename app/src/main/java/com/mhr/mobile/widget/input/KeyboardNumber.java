package com.mhr.mobile.widget.input;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.google.android.material.button.MaterialButton;
import com.mhr.mobile.R;

public class KeyboardNumber extends LinearLayout implements View.OnClickListener {

  private InputConnection inputConnection;
  private SparseArray<String> keyValue = new SparseArray<>();
  private MaterialButton button1,
      button2,
      button3,
      button4,
      button5,
      button6,
      button7,
      button8,
      button9,
      buttonOOO,
      buttonO,
      buttonControl;
  private ImageButton delete;
  private EditText targetEditText; // Tambahkan EditText sebagai target
  private Handler handler = new Handler();
  private Runnable deleteRunnable;

  public KeyboardNumber(Context context) {
    super(context);
    initialization(context);
  }

  public KeyboardNumber(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    float textSize = -1;

    if (attrs != null) {
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyboardNumber);
      textSize = a.getDimension(R.styleable.KeyboardNumber_textSize, -1);
      a.recycle();
    }

    initialization(context);

    if (textSize > 0) {
      setTextSizeInDp(textSize);
    }
  }

  public KeyboardNumber(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialization(context);
  }

  private void initialization(Context context) {
    LayoutInflater.from(context).inflate(R.layout.keyboard_number, this, true);

    button1 = findViewById(R.id.b1);
    button2 = findViewById(R.id.b2);
    button3 = findViewById(R.id.b3);
    button4 = findViewById(R.id.b4);
    button5 = findViewById(R.id.b5);
    button6 = findViewById(R.id.b6);
    button7 = findViewById(R.id.b7);
    button8 = findViewById(R.id.b8);
    button9 = findViewById(R.id.b9);
    buttonO = findViewById(R.id.bO);
    buttonOOO = findViewById(R.id.bOOO);
    buttonControl = findViewById(R.id.bControl);
    delete = findViewById(R.id.delete);

    button1.setOnClickListener(this);
    button2.setOnClickListener(this);
    button3.setOnClickListener(this);
    button4.setOnClickListener(this);
    button5.setOnClickListener(this);
    button6.setOnClickListener(this);
    button7.setOnClickListener(this);
    button8.setOnClickListener(this);
    button9.setOnClickListener(this);
    buttonO.setOnClickListener(this);
    buttonOOO.setOnClickListener(this);
    buttonControl.setOnClickListener(this);
    delete.setOnClickListener(this);
    keyValue.put(R.id.b1, "1");
    keyValue.put(R.id.b2, "2");
    keyValue.put(R.id.b3, "3");
    keyValue.put(R.id.b4, "4");
    keyValue.put(R.id.b5, "5");
    keyValue.put(R.id.b6, "6");
    keyValue.put(R.id.b7, "7");
    keyValue.put(R.id.b8, "8");
    keyValue.put(R.id.b9, "9");
    keyValue.put(R.id.bO, "0");
    keyValue.put(R.id.bOOO, "000");

    setupDeleteButton();
    for (int i = 0; i < getChildCount(); i++) {
      View v = getChildAt(i);
      v.setHapticFeedbackEnabled(true);
    }
  }

  private void vibrate(View view) {
    // vibration ketika di tekan
    view.performHapticFeedback(
        HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
  }

  @Override
  public void onClick(View view) {
    if (inputConnection == null) {
      return;
    }
    vibrate(view);

    // Fokuskan EditText
    if (targetEditText != null) {
      targetEditText.requestFocus();
    }

    if (view.getId() == R.id.bControl) {
      selectAll();
      return;
    }

    if (view.getId() == R.id.delete) {
      deleteCharacter();
    } else {
      String stringValue = keyValue.get(view.getId());
      inputConnection.commitText(stringValue, 1);
    }
  }

  private void deleteCharacter() {
    if (inputConnection == null) {
      return;
    }

    CharSequence selectedText = inputConnection.getSelectedText(0);
    if (TextUtils.isEmpty(selectedText)) {
      inputConnection.deleteSurroundingText(1, 0);
    } else {
      inputConnection.commitText("", 1);
    }
  }

  private void selectAll() {
    if (targetEditText != null) {
      targetEditText.requestFocus();
      targetEditText.selectAll();
    }
  }

  private void setupDeleteButton() {
    delete.setOnTouchListener(
        (v, event) -> {
          switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
              vibrate(v);
              startDeleting();
              return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
              stopDeleting();
              return true;
          }
          return false;
        });
  }

  private void startDeleting() {
    if (deleteRunnable == null) {
      deleteRunnable =
          new Runnable() {
            @Override
            public void run() {
              vibrate(delete);
              deleteCharacter();
              handler.postDelayed(this, 130);
            }
          };
    }
    handler.post(deleteRunnable);
  }

  private void stopDeleting() {
    if (deleteRunnable != null) {
      handler.removeCallbacks(deleteRunnable);
    }
  }

  public void setInputConnection(InputConnection inputConnectionLocal) {
    this.inputConnection = inputConnectionLocal;
  }

  // Tambahkan metode untuk mengatur target EditText
  public void setTargetEditText(EditText editText) {
    this.targetEditText = editText;
  }

  public void setTextSizeInDp(float sizePx) {
    button1.setTextSize(pxToSp(sizePx));
    button2.setTextSize(pxToSp(sizePx));
    button3.setTextSize(pxToSp(sizePx));
    button4.setTextSize(pxToSp(sizePx));
    button5.setTextSize(pxToSp(sizePx));
    button6.setTextSize(pxToSp(sizePx));
    button7.setTextSize(pxToSp(sizePx));
    button8.setTextSize(pxToSp(sizePx));
    button9.setTextSize(pxToSp(sizePx));
    buttonO.setTextSize(pxToSp(sizePx));
    // buttonOOO.setTextSize(pxToSp(sizePx));
    // buttonControl.setTextSize(pxToSp(sizePx));
  }

  // Konversi pixel ke sp
  private float pxToSp(float px) {
    return px / getResources().getDisplayMetrics().scaledDensity;
  }

  public void setTextSize(int size) {
    button1.setTextSize(size);
    button2.setTextSize(size);
    button3.setTextSize(size);
    button4.setTextSize(size);
    button5.setTextSize(size);
    button6.setTextSize(size);
    button7.setTextSize(size);
    button8.setTextSize(size);
    button9.setTextSize(size);
    buttonO.setTextSize(size);
  }

  public void setButtonEnabled(boolean enabled) {
    button1.setEnabled(enabled);
    button2.setEnabled(enabled);
    button3.setEnabled(enabled);
    button4.setEnabled(enabled);
    button5.setEnabled(enabled);
    button6.setEnabled(enabled);
    button7.setEnabled(enabled);
    button8.setEnabled(enabled);
    button9.setEnabled(enabled);
    buttonO.setEnabled(enabled);
	
	button1.setClickable(enabled);
    button2.setClickable(enabled);
    button3.setClickable(enabled);
    button4.setClickable(enabled);
    button5.setClickable(enabled);
    button6.setClickable(enabled);
    button7.setClickable(enabled);
    button8.setClickable(enabled);
    button9.setClickable(enabled);
    buttonO.setClickable(enabled);
  }
}

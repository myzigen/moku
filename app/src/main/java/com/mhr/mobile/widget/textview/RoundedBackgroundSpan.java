package com.mhr.mobile.widget.textview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

public class RoundedBackgroundSpan extends ReplacementSpan {
  private final int backgroundColor;
  private final int textColor;
  private final int radius;

  public RoundedBackgroundSpan(int backgroundColor, int textColor, int radius) {
    this.backgroundColor = backgroundColor;
    this.textColor = textColor;
    this.radius = radius;
  }

  @Override
  public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
    return Math.round(paint.measureText(text, start, end));
  }

  @Override
  public void draw(
      Canvas canvas,
      CharSequence text,
      int start,
      int end,
      float x,
      int top,
      int y,
      int bottom,
      Paint paint) {
    float width = paint.measureText(text, start, end);
    float height = paint.descent() - paint.ascent();

    RectF rect = new RectF(x, y + paint.ascent(), x + width, y + paint.descent());
    Paint paintBackground = new Paint();
    paintBackground.setColor(backgroundColor);
    paintBackground.setAntiAlias(true);

    canvas.drawRoundRect(rect, radius, radius, paintBackground);

    paint.setColor(textColor);
    canvas.drawText(text, start, end, x, y, paint);
  }
}

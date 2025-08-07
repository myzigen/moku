package com.mhr.mobile.widget.ticketview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import com.mhr.mobile.R;

public class TicketView2 extends RelativeLayout {
  private static final float DEFAULT_RADIUS = 9f;
  private static final int NO_VALUE = -1;

  private final Paint eraser = new Paint(Paint.ANTI_ALIAS_FLAG);
  private int anchorViewId1 = 0;
  private int anchorViewId2 = 0;
  private Path circlesPath = new Path();
  private float circlePosition1 = 0f;
  private float circlePosition2 = 0f;
  private float circleRadius = 0f;
  private float circleSpace = 0f;
  private int dashColor = 0;
  private float dashSize = 0f;
  private final Path dashPath = new Path();
  private final Paint dashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

  public TicketView2(Context context) {
    this(context, null);
  }

  public TicketView2(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TicketView2(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setLayerType(View.LAYER_TYPE_HARDWARE, null);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TicketView2);
    try {
      circleRadius = a.getDimension(R.styleable.TicketView2_tv_circleRadius, getDp(DEFAULT_RADIUS));
      anchorViewId1 = a.getResourceId(R.styleable.TicketView2_tv_anchor1, NO_VALUE);
      anchorViewId2 = a.getResourceId(R.styleable.TicketView2_tv_anchor2, NO_VALUE);
      circleSpace = a.getDimension(R.styleable.TicketView2_tv_circleSpace, getDp(15f));
      dashColor = a.getColor(R.styleable.TicketView2_tv_dashColor, Color.parseColor("#0085be"));
      dashSize = a.getDimension(R.styleable.TicketView2_tv_dashSize, getDp(1.5f));
    } finally {
      a.recycle();
    }

    eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

    dashPaint.setColor(dashColor);
    dashPaint.setStyle(Paint.Style.STROKE);
    dashPaint.setStrokeWidth(dashSize);
    dashPaint.setPathEffect(new DashPathEffect(new float[] {getDp(3f), getDp(3f)}, 0));
  }

  public void setRadius(float radius) {
    this.circleRadius = radius;
    postInvalidate();
  }

  public void setAnchor(View view1, View view2) {
    Rect rect = new Rect();
    if (view1 != null) {
      view1.getDrawingRect(rect);
      offsetDescendantRectToMyCoords(view1, rect);
      circlePosition1 = rect.bottom;
    }

    if (view2 != null) {
      view2.getDrawingRect(rect);
      offsetDescendantRectToMyCoords(view2, rect);
      circlePosition2 = rect.bottom;
    }

    postInvalidate();
  }

  @Override
  protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
    boolean drawChild = super.drawChild(canvas, child, drawingTime);
    drawHoles(canvas);
    return drawChild;
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    canvas.save();
    super.dispatchDraw(canvas);
    canvas.restore();
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    if (anchorViewId1 != NO_VALUE || anchorViewId2 != NO_VALUE) {
      final View anchorView1 = findViewById(anchorViewId1);
      final View anchorView2 = findViewById(anchorViewId2);
      getViewTreeObserver()
          .addOnGlobalLayoutListener(
              new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                  } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                  }
                  setAnchor(anchorView1, anchorView2);
                }
              });
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    drawHoles(canvas);
    super.onDraw(canvas);
  }

  private void drawHoles(Canvas canvas) {
    circlesPath = new Path();
    int w = getWidth();
    float radius = circleRadius;
    float space = circleSpace;
    float circleWidth = radius * 2;

    int leftMargin = 0;
    if (getLayoutParams() instanceof MarginLayoutParams) {
      MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
      leftMargin = lp.leftMargin;
    }

    int left = getLeft() - leftMargin;
    float circleSpace = circleWidth + space;
    int count = (int) (w / circleSpace);
    float offset = w - circleSpace * count;
    float sideOffset = offset / 2;
    float halfCircleSpace = circleSpace / 2;

    for (int i = 0; i < count; i++) {
      float positionCircle = i * circleSpace + sideOffset + left - radius;
      if (i == 0) {
        positionCircle = left + sideOffset - radius;
      }
      circlesPath.addCircle(
          positionCircle + halfCircleSpace, -circleRadius / 4, radius, Path.Direction.CW);
    }

    // anchor1
    circlesPath.addCircle(-circleRadius / 4, circlePosition1, radius, Path.Direction.CW);
    circlesPath.addCircle(w + circleRadius / 4, circlePosition1, radius, Path.Direction.CW);

    // anchor2
    if (anchorViewId2 != NO_VALUE) {
      circlesPath.addCircle(-circleRadius / 4, circlePosition2, radius, Path.Direction.CW);
      circlesPath.addCircle(w + circleRadius / 4, circlePosition2, radius, Path.Direction.CW);
    }

    dashPath.reset();

    dashPath.moveTo(circleRadius, circlePosition1);
    dashPath.quadTo(w - circleRadius, circlePosition1, w - circleRadius, circlePosition1);

    if (anchorViewId2 != NO_VALUE) {
      dashPath.moveTo(circleRadius, circlePosition2);
      dashPath.quadTo(w - circleRadius, circlePosition2, w - circleRadius, circlePosition2);
    }

    if (dashSize > 0) {
      canvas.drawPath(dashPath, dashPaint);
    }
    canvas.drawPath(circlesPath, eraser);
  }

  private float getDp(float value) {
    if (value == 0f) return 0;
    float density = getResources().getDisplayMetrics().density;
    return (float) Math.ceil(density * value);
  }
}

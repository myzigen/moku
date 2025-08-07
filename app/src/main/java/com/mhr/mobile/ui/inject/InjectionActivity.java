package com.mhr.mobile.ui.inject;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.transition.platform.MaterialSharedAxis;
import com.mhr.mobile.R;
import com.mhr.mobile.interfaces.NavigationCallback;
import com.mhr.mobile.ui.dialog.LoadingDialogFragment;
import com.mhr.mobile.ui.intro.UserSession;
import com.mhr.mobile.util.QiosPreferences;
import com.mhr.mobile.util.UtilsManager;
import com.mhr.mobile.viewmodel.CacheViewModel;
import com.mhr.mobile.widget.input.KeyboardNumberCustom;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;

public abstract class InjectionActivity extends InjectionConnectivity
    implements NavigationCallback {
  public static final String EXTRA_DEMO_TITLE = "title";
  protected ViewGroup containerView;
  private View rootContainer;
  protected AppBarLayout appbar;
  protected MaterialToolbar toolbar;
  protected LinearLayoutManager linearLayoutManager;
  protected GridLayoutManager gridLayoutManager;
  protected SpacingItemDecoration spacingItemDecoration;
  private KeyboardNumberCustom keyboard;
  private BottomSheetBehavior<View> behavior;
  protected QiosPreferences pref;
  protected LoadingDialogFragment dialog;
  protected CacheViewModel cacheViewModel;
  protected UserSession session;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setDrawUnderStatusbar();
	transitionSharedAxis(R.id.rootContainer);
    setContentView(R.layout.activity_container);
    pref = new QiosPreferences(this);
	AppCompatDelegate.setDefaultNightMode(pref.getThemeMode());
    // UtilsManager.getStatusBarColor(getWindow(), this);
    session = UserSession.with(this);
    dialog = new LoadingDialogFragment();

    cacheViewModel = new ViewModelProvider(this).get(CacheViewModel.class);

    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    rootContainer = findViewById(R.id.rootContainer);
    keyboard = findViewById(R.id.keyboard);
    appbar = findViewById(R.id.appbar);
    toolbar = findViewById(R.id.toolbar);
    setDefaultTitleToolbar(toolbar);

    toolbar.setNavigationOnClickListener(v -> onBackPressed());

    // Menambahkan layout utama ke container
    containerView = findViewById(R.id.idActivity_container);
    View contentView =
        onCreateQiosView(LayoutInflater.from(this), containerView, savedInstanceState);
    containerView.addView(contentView);
  }

  @Override
  protected void onPhoneNumberPicked(String phoneNumber) {}

  @Override
  protected void onPhoneNumberPickFailed() {}

  /**
   * Mengatur layout utama dari activity ini.
   *
   * @param layoutInflater untuk inflating view
   * @param viewGroup parent view
   * @param bundle instance state
   * @return view utama
   */
  public abstract View onCreateQiosView(
      LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle);

  /**
   * Menentukan judul toolbar.
   *
   * @return judul
   */
  protected String getTitleToolbar() {
    return "";
  }

  /**
   * Mengatur judul toolbar default.
   *
   * @param toolbar toolbar
   */
  private void setDefaultTitleToolbar(Toolbar toolbar) {
    if (getTitleToolbar() != null && !getTitleToolbar().isEmpty()) {
      toolbar.setTitle(getTitleToolbar());
    } else {
      toolbar.setTitle(getDefaultDemoTitle());
    }
  }

  /**
   * Mengambil judul demo default jika tidak ada judul disediakan.
   *
   * @return judul demo
   */
  private String getDefaultDemoTitle() {
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      return extras.getString(EXTRA_DEMO_TITLE, "");
    }
    return "";
  }

  protected LinearLayoutManager getLinearLayoutManager() {
    return linearLayoutManager = new LinearLayoutManager(this);
  }

  protected GridLayoutManager getGridLayoutManager(int count) {
    return gridLayoutManager = new GridLayoutManager(this, count);
  }

  protected SpacingItemDecoration getSpacingItemDecoration(int column, int count, boolean spacing) {
    return spacingItemDecoration = new SpacingItemDecoration(column, count, spacing);
  }

  public void targetActivity(Class<?> targetActivity) {
    isClicked();
    Intent i = new Intent(this, targetActivity);
    abStartActivity(i);
  }

  public void isClicked() {
    if (!UtilsManager.allowClick()) return;
  }

  protected String getAbsIntent(String key) {
    Intent intent = getIntent();
    return intent != null ? intent.getStringExtra(key) : null;
  }

  protected void initEditext(EditText editText) {
    View viewKeyboard = keyboard;
    editText.setShowSoftInputOnFocus(false);

    EditorInfo editorInfo = new EditorInfo();
    InputConnection inputConnection = editText.onCreateInputConnection(editorInfo);
    if (inputConnection != null) {
      keyboard.setInputConnection(inputConnection);
      keyboard.setTargetEditText(editText);
    }

    behavior = BottomSheetBehavior.from(viewKeyboard);
    behavior.setHideable(true);
    behavior.setDraggable(false);
    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    // Saat disentuh
    editText.setOnTouchListener(
        (v, event) -> {
          if (event.getAction() == MotionEvent.ACTION_UP) {
            editText.requestFocus();

            keyboard.setInputConnection(editText.onCreateInputConnection(new EditorInfo()));
            keyboard.setTargetEditText(editText);

            if (behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
              behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
          }
          return false;
        });

    // Selesai klik di keyboard
    keyboard.setOnSelesaiClickListener(
        v -> {
          if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            EditText target = keyboard.getTargetEditText();
            if (target != null) target.clearFocus();
          }
        });
  }

  protected void showKeyboard() {
    if (behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
      behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
  }

  protected void hideKeyboard() {
    if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
      behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
  }

  @Override
  public void abStartActivity(Intent intent) {
    int axis = MaterialSharedAxis.Y;
    intent.putExtra("SHARED_AXIS_KEY", axis);
	intent.putExtra("save_axis", true);
	intent.putExtra("refresh_produk", true);
    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
    startActivity(intent, options.toBundle());
  }
  
  @Override
  public void onBackPressed(){
	  super.onBackPressed();
  }

  protected void transitionSharedAxis(@IdRes int id) {
    getWindow().setAllowEnterTransitionOverlap(false);
    //getWindow().setAllowReturnTransitionOverlap(false);
    int axis = getIntent().getIntExtra("SHARED_AXIS_KEY", MaterialSharedAxis.Y);

    MaterialSharedAxis enterTransition = new MaterialSharedAxis(axis, true);
    enterTransition.setDuration(500);
    enterTransition.addTarget(id);
    getWindow().setEnterTransition(enterTransition);

    MaterialSharedAxis returnTransition = new MaterialSharedAxis(axis, false);
    returnTransition.setDuration(500);
    returnTransition.addTarget(id);
    getWindow().setReturnTransition(returnTransition);

    MaterialSharedAxis reenter = new MaterialSharedAxis(axis, false);
    reenter.addTarget(id);
    getWindow().setReenterTransition(reenter);
  }
}

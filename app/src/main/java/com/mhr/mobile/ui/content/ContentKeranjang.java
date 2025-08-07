package com.mhr.mobile.ui.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.adapter.CartAdapter;
import com.mhr.mobile.api.request.cart.CartRequest;
import com.mhr.mobile.api.response.cart.CartResponse;
import com.mhr.mobile.databinding.InjectionRecyclerviewBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidViews;
import java.util.ArrayList;
import java.util.List;

public class ContentKeranjang extends InjectionActivity {
  private InjectionRecyclerviewBinding binding;
  private CartAdapter adapter;

  @Override
  protected String getTitleToolbar() {
    return "Favorite";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = InjectionRecyclerviewBinding.inflate(getLayoutInflater());
    loadCart();
    return binding.getRoot();
  }

  private void loadCart() {
    binding.recyclerview.addItemDecoration(getSpacingItemDecoration(1, 30, true));
    binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    adapter = new CartAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnDeleteListener(
        id -> {
          new CartRequest(this)
              .requestDeleteCart(
                  id,
                  response -> {
                    AndroidViews.showSnackbar(this, "Dihapus");
                    adapter.removeItem(id);
                  });
        });

    new CartRequest(this)
        .requestGetCart(
            response -> {
              List<CartResponse.CartItem> list = response.cart;
              if (list != null && !list.isEmpty()) {
                adapter.addAll(response.cart);
                binding.expandable.collapse();
              } else {
                binding.expandable.expand();
				binding.infoCatatan.setText("Belum ada Favorite di tambahkan");
				binding.infoPenjelasan.setVisibility(View.GONE);
              }
            });
  }
}

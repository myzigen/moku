package com.mhr.mobile.manage.call;

import com.mhr.mobile.manage.response.MarketplaceResponse;
import com.mhr.mobile.manage.response.SliderHomeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BukaolshopEndpoint {
  @GET("app/slide")
  Call<SliderHomeResponse> getGambarSlide(@Query("token") String token);

  @GET("app/produk")
  Call<MarketplaceResponse> getProduk(@Query("token") String token, @Query("page") int page);
}

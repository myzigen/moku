package com.mhr.mobile.api.endpoint;

import com.google.gson.JsonObject;
import com.mhr.mobile.api.request.duitku.DuitkuResponse;
import com.mhr.mobile.api.response.ResponseCheckout;
import com.mhr.mobile.api.response.ResponseHistory;
import com.mhr.mobile.api.response.ResponseInquiryPasca;
import com.mhr.mobile.api.response.ResponseOtp;
import com.mhr.mobile.api.response.ResponsePelanggan;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.api.response.ResponsePromosi;
import com.mhr.mobile.api.response.ResponseRegister;
import com.mhr.mobile.api.response.ResponseSetting;
import com.mhr.mobile.api.response.ResponseTopup;
import com.mhr.mobile.api.response.ResponseTransaksi;
import com.mhr.mobile.api.response.ResponseTransaksiPasca;
import com.mhr.mobile.api.response.ResponseTripay;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.api.response.cart.CartResponse;
import com.mhr.mobile.api.response.inquiry.WalletResponse;
import com.mhr.mobile.model.Wilayah;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface EndpointApi {
  @POST("api/users/users_pelanggan.php")
  Call<ResponsePelanggan> kelolaPelanggan(@Body RequestBody body);

  @GET("api/get/get_riwayat_last_number.php")
  Call<ResponseHistory> getLastNumber(
      @Query("token") String token,
      @Query("kategori") String kategori,
      @Query("jenis") String jenis);

  @GET("api/inquiry/check_inquiry_wallet.php")
  Call<WalletResponse> getWalletData(@Query("nomor") String nomor);

  @FormUrlEncoded
  @POST("api/inquiry/save_inquiry_wallet.php")
  Call<WalletResponse> saveWalletData(
      @Field("nomor") String nomor, @Field("nama") String nama, @Field("brand") String brand);

  @POST("api/dao/add_keranjang_item.php")
  Call<CartResponse> addCartItem(@Body CartResponse.CartItem item);

  @POST("api/del/delete_keranjang_item.php")
  Call<CartResponse> deleteCartItem(@Body CartResponse.CartItem item);

  @POST("api/get/get_keranjang_item.php")
  Call<CartResponse> getCartItem(@Query("token") String token);

  @Multipart
  @POST("api/users/users_register.php")
  Call<ResponseRegister> endpointUserRegister(
      @Part("nomor") RequestBody nomor,
      @Part("nama") RequestBody nama,
      @Part("nama_toko") RequestBody namaToko,
      @Part("referral") RequestBody referral,
      @Part("pin") RequestBody pin,
      @Part("player_id") RequestBody playerId,
      @Part MultipartBody.Part foto);

  @FormUrlEncoded
  @POST("api/otp_request.php")
  Call<ResponseBody> endpointOtp(@Field("nomor") String nomor);

  @FormUrlEncoded
  @POST("api/otp_verify.php")
  Call<ResponseOtp> endpointVerifyOtp(@Field("nomor") String nomor, @Field("otp") String otp);

  @FormUrlEncoded
  @POST("api/check-nomor-terdaftar.php")
  Call<ResponseUsers> checkNomorTerdaftar(@Field("nomor") String nomor);

  @FormUrlEncoded
  @POST("client/save_users.php") // Update Profile
  Call<ResponseBody> updateProfile(
      @Header("Authorization") String token,
      @Field("nama") String nama,
      @Field("nama_toko") String namaToko,
      @Field("alamat_toko") String alamatToko);

  @POST("api/del/delete_users.php")
  Call<ResponseBody> deleteAkun(@Header("Authorization") String token, @Body RequestBody body);

  @POST("api/users/users_data.php") // ambil data profile
  Call<ResponseUsers> getUsers(@Body RequestBody body);

  @POST("api/users/users_cek_referral.php") // ambil data profile
  Call<ResponseRegister> checkReferral(@Body RequestBody body);

  @POST("api/users/users_verify_pin.php")
  Call<ResponseUsers> verifyPin(@Body RequestBody body);

  @GET("api/get/get_pricelist.php")
  Call<List<ResponsePricelist>> getProduk(
      @Query("token") String token, @Query("tipe") String tipe, @Query("kategori") String kategori);

  @POST("api/transaksi.php")
  Call<ResponseTransaksi> getTransaksi(@Body RequestBody body);

  @POST("api/check-pasca.php")
  Call<ResponseInquiryPasca> getInquiryPasca(@Body RequestBody body);

  @POST("api/transaksi-pasca.php")
  Call<ResponseTransaksiPasca> getTransaksiPasca(@Body RequestBody body);

  @GET("api/transaksi_history.php")
  Call<ResponseHistory> getTransaksiHistory(
      @Query("token") String token,
      @Query("page") int page,
      @Query("limit") int limit,
      @Query("kategori") String kategori);

  @FormUrlEncoded
  @POST("client/users/tambah_pelanggan.php")
  Call<ResponsePelanggan> tambahPelanggan(
      @Field("token") String token,
      @Field("nama") String nama,
      @Field("no_hp") String noHp,
      @Field("token_listrik") String tknListrik,
      @Field("tagihan_wifi") String tghnWifi,
      @Field("catatan") String catatan);

  @FormUrlEncoded
  @POST("client/users/get_pelanggan.php")
  Call<ResponsePelanggan> getPelanggan(@Field("token") String token);

  @GET("digiflazz/client/refresh_produk.php")
  Call<JsonObject> getRefreshProduk();

  @GET("api-sandbox/merchant/payment-channel")
  Call<ResponseTripay> getPaymentTripay(@Header("Authorization") String authHeader);

  @POST("myapi/daftar_topup.php")
  Call<ResponseTopup> getTopup(@Body RequestBody body);

  @POST("api-sandbox/transaction/create")
  Call<ResponseCheckout> getPaymentCheckout(
      @Header("Authorization") String auth, @Body RequestBody body);

  @POST("digiflazz/database/tripay/tripay_simpan_transaksi.php")
  Call<ResponseBody> getSimpanTransaksi(@Body RequestBody body);

  @GET("digiflazz/database/tripay/tripay_get_transaksi.php")
  Call<ResponseHistory> getDataTransaksi(@Query("user_id") String userid);

  @GET("myapi/wilayah_search.php")
  Call<Wilayah> searchKecamatan(@Query("q") String keyword);

  @POST("api/gateway/duitku.php")
  Call<DuitkuResponse> getDuitku(@Body RequestBody body);

  @POST("api/gateway/duitku_payment.php")
  Call<DuitkuResponse> getDuitkuPayment(@Body RequestBody body);

  @GET("api/digiflazz/cek-status.php")
  Call<ResponseTransaksi> getCekStatus(@Query("ref_id") String refId);

  @GET("admin/promosi/flashsale_aktif.php")
  Call<List<ResponsePricelist>> getFlashsale();

  @GET("admin/setting/maintance.php")
  Call<ResponseSetting> getMaintance();

  @GET("admin/promosi/show_popup.php")
  Call<ResponsePromosi> showPopup();
}

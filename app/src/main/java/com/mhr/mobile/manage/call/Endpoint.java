package com.mhr.mobile.manage.call;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Endpoint {


  @Multipart
  @POST("Qiospro/v1/data/upload_image.php")
  Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);
}

/*
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("upload_image.php")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);
}
*/

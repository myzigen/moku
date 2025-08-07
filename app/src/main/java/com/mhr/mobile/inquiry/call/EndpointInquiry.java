package com.mhr.mobile.inquiry.call;

import com.mhr.mobile.inquiry.response.InquiryPLNResponse;
import com.mhr.mobile.inquiry.response.InquiryResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface EndpointInquiry {

  @Headers("Content-type: application/json")
  @POST("api/v1/bill/check")
  Call<InquiryResponse> getInquiry(@Body RequestBody body);

  @POST("api/inquiry-pln")
  Call<InquiryPLNResponse> getInquiryPln(@Body RequestBody body);
}

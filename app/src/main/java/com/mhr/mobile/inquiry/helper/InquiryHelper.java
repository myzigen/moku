package com.mhr.mobile.inquiry.helper;

import com.mhr.mobile.inquiry.response.InquiryResponse;


public class InquiryHelper {
	
	public static InquiryHelper instance;
	private InquiryResponse.Data data;
	private InquiryHelper(){
		
	}
	
	public static InquiryHelper getInstance(){
		if (instance == null){
			instance = new InquiryHelper();
		}
		return instance;
	}
	
	public InquiryResponse.Data getData(){
		return data;
	}
	public void setData(InquiryResponse.Data data){
		this.data = data;
	}
}

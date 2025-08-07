package com.mhr.mobile.api.response;

public class ResponsePromosi {
  public boolean status;
  public Data data;

  public static class Data {
    public String image_url;
    public String status;
	
	public String getImageUrl(){
		return image_url;
	}
  }
}

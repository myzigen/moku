package com.mhr.mobile.api.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResponseHistory {
  private boolean status;

  @SerializedName("data")
  private DataContainer data;

  public boolean isStatus() {
    return status;
  }

  public DataContainer getData() {
    return data;
  }

  public static class DataContainer {
    private int page;
    private int limit;
    private int total;
    private String nomor;

    @SerializedName("items")
    private List<Data> items;

    public int getPage() {
      return page;
    }

    public int getLimit() {
      return limit;
    }

    public int getTotal() {
      return total;
    }

    public String getNomor() {
      return nomor;
    }

    public List<Data> getItems() {
      return items;
    }
  }

  public static class Data {
    private int id;
    private String kategori;
    private String jenis;
    private String keterangan;
    private int jumlah;
    private String status;
    private String tanggal;
    private String ref_id;
    private String brand;
    private String brand_icon_url;

    // ... getter lengkap
    public int getId() {
      return id;
    }

    public String getKategori() {
      return kategori;
    }

    public String getJenis() {
      return jenis;
    }

    public String getKeterangan() {
      return keterangan;
    }

    public int getJumlah() {
      return jumlah;
    }

    public String getStatus() {
      return status;
    }
	
	public void setStatus(String status){
		this.status = status;
	}

    public String getTanggal() {
      return tanggal;
    }

    public String getRefId() {
      return ref_id;
    }

    public String getBrand() {
      return brand;
    }

    public String getBrandIconUrl() {
      return brand_icon_url;
    }
  }
}

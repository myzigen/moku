package com.mhr.mobile.model;
import java.util.List;

public class Wilayah {
  public String status;
  
  public List<Item> data;

  public static class Item {
    public String id;
    public String nama;
    public String kabupaten;
    public String provinsi;

    public String getFullNama() {
      return nama + " - " + kabupaten + " - " + provinsi;
    }
  }
}

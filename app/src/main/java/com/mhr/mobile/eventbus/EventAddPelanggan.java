package com.mhr.mobile.eventbus;

public class EventAddPelanggan {
  public String aksi, id;
  public int total;

  public EventAddPelanggan() {}

  public EventAddPelanggan(int total) {
    this.total = total;
  }

  public EventAddPelanggan(String aksi, String id) {
    this.aksi = aksi;
    this.id = id;
  }
}

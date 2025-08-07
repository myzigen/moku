// Di file Desc.java
package com.mhr.mobile.api.response;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Desc implements Parcelable {
  public String tarif;
  public int daya;
  public int lembar_tagihan;
  public String alamat;
  public String jatuh_tempo;
  public String jumlah_peserta;
  public String item_name;
  public String no_rangka;
  public String no_pol;
  public String tenor;
  public String tahun_pajak;
  public String kelurahan;
  public String kecamatan;
  public String kode_kab_kota;
  public String kab_kota;
  public String luas_tanah;
  public String luas_gedung;
  public String transaksi;
  public String no_registrasi;
  public String tanggal_registrasi;

  public List<DetailUniversal> detail;

  protected Desc(Parcel in) {
    tarif = in.readString();
    daya = in.readInt();
    lembar_tagihan = in.readInt();
    alamat = in.readString();
    jatuh_tempo = in.readString();
    jumlah_peserta = in.readString();
    item_name = in.readString();
    no_rangka = in.readString();
    no_pol = in.readString();
    tenor = in.readString();
    tahun_pajak = in.readString();
    kelurahan = in.readString();
    kecamatan = in.readString();
    kode_kab_kota = in.readString();
    kab_kota = in.readString();
    luas_tanah = in.readString();
    luas_gedung = in.readString();
    transaksi = in.readString();
    no_registrasi = in.readString();
    tanggal_registrasi = in.readString();
    detail = new ArrayList<>();
    in.readList(detail, DetailUniversal.class.getClassLoader());
  }

  public static final Creator<Desc> CREATOR = new Creator<Desc>() {
    @Override
    public Desc createFromParcel(Parcel in) {
      return new Desc(in);
    }

    @Override
    public Desc[] newArray(int size) {
      return new Desc[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(tarif);
    dest.writeInt(daya);
    dest.writeInt(lembar_tagihan);
    dest.writeString(alamat);
    dest.writeString(jatuh_tempo);
    dest.writeString(jumlah_peserta);
    dest.writeString(item_name);
    dest.writeString(no_rangka);
    dest.writeString(no_pol);
    dest.writeString(tenor);
    dest.writeString(tahun_pajak);
    dest.writeString(kelurahan);
    dest.writeString(kecamatan);
    dest.writeString(kode_kab_kota);
    dest.writeString(kab_kota);
    dest.writeString(luas_tanah);
    dest.writeString(luas_gedung);
    dest.writeString(transaksi);
    dest.writeString(no_registrasi);
    dest.writeString(tanggal_registrasi);
    dest.writeList(detail);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static class DetailUniversal implements Parcelable {
    public String periode;
    public String nilai_tagihan;
    public String admin;
    public String denda;
    public String meter_awal;
    public String meter_akhir;
    public String biaya_lain;
    public String no_ref;

    protected DetailUniversal(Parcel in) {
      periode = in.readString();
      nilai_tagihan = in.readString();
      admin = in.readString();
      denda = in.readString();
      meter_awal = in.readString();
      meter_akhir = in.readString();
      biaya_lain = in.readString();
      no_ref = in.readString();
    }

    public static final Creator<DetailUniversal> CREATOR = new Creator<DetailUniversal>() {
      @Override
      public DetailUniversal createFromParcel(Parcel in) {
        return new DetailUniversal(in);
      }

      @Override
      public DetailUniversal[] newArray(int size) {
        return new DetailUniversal[size];
      }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(periode);
      dest.writeString(nilai_tagihan);
      dest.writeString(admin);
      dest.writeString(denda);
      dest.writeString(meter_awal);
      dest.writeString(meter_akhir);
      dest.writeString(biaya_lain);
      dest.writeString(no_ref);
    }

    @Override
    public int describeContents() {
      return 0;
    }
  }
}
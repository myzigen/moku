package com.mhr.mobile.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormatUtils {

  public static String normalizeNomor(String nomor) {
    if (nomor == null || nomor.trim().isEmpty()) {
      return "";
    }

    nomor = nomor.replaceAll("[^0-9+]", ""); // hanya angka dan tanda +

    if (nomor.startsWith("0")) {
      return "62" + nomor.substring(1);
    } else if (nomor.startsWith("+62")) {
      return nomor.replace("+", "");
    }

    return nomor;
  }

  public static String denormalizeNomor(String nomor) {
    if (nomor == null || nomor.trim().isEmpty()) {
      return "";
    }

    // Ubah dari 62xxxxxx ke 08xxxxxx
    if (nomor.startsWith("62")) {
      return "0" + nomor.substring(2);
    }

    return nomor;
  }

  public static String initialName(String nama) {
    String inisial = "";
    if (nama != null && !nama.isEmpty()) {
      inisial = nama.substring(0, 1).toUpperCase();
    }
    return inisial;
  }

  public static String maskingNomor(String nomor) {
    if (nomor.length() < 7) return nomor; // Jangan masking jika terlalu pendek

    String awal = nomor.substring(0, 3);
    String akhir = nomor.substring(nomor.length() - 3);
    String tengah = "******"; // Bisa disesuaikan panjangnya

    return awal + tengah + akhir;
  }

  public static String formatRupiah(double number) {
    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    format.setMaximumFractionDigits(0); // Menghilangkan desimal
    return format.format(number);
  }

  public static String formatRupiah(int amount) {
    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    format.setMinimumFractionDigits(0); // Tidak menampilkan desimal
    format.setMaximumFractionDigits(0);

    // Format ke Rupiah
    String formattedAmount = format.format(amount);

    // Pastikan simbol Rupiah tetap seragam
    return formattedAmount.replace("Rp", "Rp").trim();
  }

  // Format string menjadi format rupiah
  public static String formatRupiah(String number) {
    long value = Long.parseLong(number); // Mengubah string menjadi angka
    DecimalFormat format = new DecimalFormat("Rp #,###");
    format.setMaximumFractionDigits(0); // Menghilangkan desimal
    return format.format(value); // Mengembalikan format rupiah
  }

  // Mengubah format Rupiah (Rp10.000) ke integer
  public static int parseRupiahToInt(String rupiah) {
    try {
      // Menghapus simbol Rp, titik, dan spasi
      String cleaned = rupiah.replace("Rp", "").replace(".", "").trim();
      return Integer.parseInt(cleaned);
    } catch (NumberFormatException e) {
      // Log.e("Util", "Error parsing Rupiah to Integer: " + rupiah, e);
      return 0;
    }
  }

  public static String hanyaAngka(String text) {
    return text.replaceAll("[^0-9]", ""); // Hanya menyisakan angka
  }

  public static String formatAngkaPendekFromString(String text) {
    // Mengonversi string ke angka (long)
    try {
      long number = Long.parseLong(text.replaceAll("[^0-9]", "")); // Menghapus non-digit karakter
      return formatAngkaPendek(number);
    } catch (NumberFormatException e) {
      return text; // Jika gagal konversi, kembalikan string asli
    }
  }

  private static String formatAngkaPendek(long number) {
    if (number >= 1_000_000) {
      return (number / 1_000_000) + "Jt";
    } else if (number >= 1_000) {
      return (number / 1_000) + "Rb";
    } else {
      return String.valueOf(number);
    }
  }

  public static String formatTimestamp(Long timestamp) {
    if (timestamp == null) {
      return "Tanggal tidak tersedia";
    }
    return formatDate(timestamp);
  }

  private static String formatDate(long timestamp) {
    Date date = new Date(timestamp);

    // Menggunakan Locale Indonesia agar nama bulan ditampilkan dalam Bahasa Indonesia
    SimpleDateFormat dateFormat =
        new SimpleDateFormat("d MMMM - HH:mm 'WIB'", new Locale("id", "ID"));
    return dateFormat.format(date);
  }

  public static boolean isStartEndCutOff(String start, String end) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
      Date now = sdf.parse(sdf.format(new Date())); // waktu sekarang

      Date startTime = sdf.parse(start);
      Date endTime = sdf.parse(end);

      Calendar nowCal = Calendar.getInstance();
      nowCal.setTime(now);

      Calendar startCal = Calendar.getInstance();
      startCal.setTime(startTime);

      Calendar endCal = Calendar.getInstance();
      endCal.setTime(endTime);

      // Tangani jika rentang waktu melewati tengah malam (misal 23:00 ke 01:00)
      if (endCal.before(startCal)) {
        if (nowCal.before(startCal)) {
          nowCal.add(Calendar.DATE, 1);
        }
        endCal.add(Calendar.DATE, 1);
      }

      return nowCal.after(startCal) && nowCal.before(endCal);

    } catch (ParseException e) {
      e.printStackTrace();
      return false;
    }
  }
}

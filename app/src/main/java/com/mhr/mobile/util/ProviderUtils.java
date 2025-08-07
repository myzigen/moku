package com.mhr.mobile.util;

import android.widget.ImageView;
import com.mhr.mobile.R;
import java.util.HashMap;
import java.util.Map;

public class ProviderUtils {

  private static final Map<String, String> prefixToProvider = new HashMap<>();

  static {
    // Telkomsel
    prefixToProvider.put("0811", "Telkomsel");
    prefixToProvider.put("0812", "Telkomsel");
    prefixToProvider.put("0813", "Telkomsel");
    prefixToProvider.put("0821", "Telkomsel");
    prefixToProvider.put("0822", "Telkomsel");
    prefixToProvider.put("0823", "Telkomsel");
    prefixToProvider.put("0851", "Telkomsel");
    prefixToProvider.put("0852", "Telkomsel");
    prefixToProvider.put("0853", "Telkomsel");

    // Indosat
    prefixToProvider.put("0814", "Indosat");
    prefixToProvider.put("0815", "Indosat");
    prefixToProvider.put("0816", "Indosat");
    prefixToProvider.put("0855", "Indosat");
    prefixToProvider.put("0856", "Indosat");
    prefixToProvider.put("0857", "Indosat");
    prefixToProvider.put("0858", "Indosat");

    // XL
    prefixToProvider.put("0817", "XL");
    prefixToProvider.put("0818", "XL");
    prefixToProvider.put("0819", "XL");
    prefixToProvider.put("0859", "XL");
    prefixToProvider.put("0877", "XL");
    prefixToProvider.put("0878", "XL");

    // Axis
    prefixToProvider.put("0831", "Axis");
    prefixToProvider.put("0832", "Axis");
    prefixToProvider.put("0837", "Axis");
    prefixToProvider.put("0838", "Axis");

    // Smartfren
    prefixToProvider.put("0881", "Smartfren");
    prefixToProvider.put("0882", "Smartfren");
    prefixToProvider.put("0883", "Smartfren");
    prefixToProvider.put("0884", "Smartfren");
    prefixToProvider.put("0885", "Smartfren");
    prefixToProvider.put("0886", "Smartfren");
    prefixToProvider.put("0887", "Smartfren");
    prefixToProvider.put("0888", "Smartfren");

    // Tri
    prefixToProvider.put("0895", "Tri");
    prefixToProvider.put("0896", "Tri");
    prefixToProvider.put("0897", "Tri");
    prefixToProvider.put("0898", "Tri");
    prefixToProvider.put("0899", "Tri");

    // by.U (special prefix)
    prefixToProvider.put("085154", "by.U");
    prefixToProvider.put("085155", "by.U");
    prefixToProvider.put("085156", "by.U");
    prefixToProvider.put("085157", "by.U");
    prefixToProvider.put("085158", "by.U");
  }

  // ✅ Deteksi apakah nomor sesuai brand
  public static boolean detectBrand(String nomor, String brand) {
    nomor = nomor.replaceAll("[^0-9]", "");
    if (nomor.length() < 4 || !nomor.startsWith("08")) return false;

    // Periksa dari 6 ke 4 digit prefix
    for (int i = 6; i >= 4; i--) {
      if (nomor.length() >= i) {
        String prefix = nomor.substring(0, i);
        String detected = prefixToProvider.get(prefix);
        if (detected != null) {
          return detected.equalsIgnoreCase(brand);
        }
      }
    }
    return false;
  }

  // ✅ Mendapatkan provider dari nomor
  public static String detectProvider(String nomor) {
    nomor = nomor.replaceAll("[^0-9]", "");

    for (int i = 6; i >= 4; i--) {
      if (nomor.length() >= i) {
        String prefix = nomor.substring(0, i);
        if (prefixToProvider.containsKey(prefix)) {
          return prefixToProvider.get(prefix);
        }
      }
    }
    return "Unknown";
  }

  public static String getPrefix(String nomor) {
    nomor = nomor.replaceAll("[^0-9]", "");
    return nomor.length() >= 4 ? nomor.substring(0, 4) : nomor;
  }

  public static String getProviderName(int position) {
    switch (position) {
      case 0:
        return "Telkomsel";
      case 1:
        return "Indosat";
      case 2:
        return "Axis";
      case 3:
        return "XL";
      case 4:
        return "Tri";
      case 5:
        return "by.U";
      default:
        return "";
    }
  }

  public static void updateProviderIcon(ImageView img, String provider) {
    int providerIcon = getProviderIconResId(provider);
    img.setImageResource(providerIcon);
  }

  public static int getProviderIconResId(String provider) {
    switch (provider.toLowerCase()) {
      case "telkomsel":
        return R.drawable.provider_telkomsel;
      case "axis":
        return R.drawable.provider_axis;
      case "xl":
        return R.drawable.provider_xl;
      case "indosat":
        return R.drawable.provider_indosat;
      case "tri":
        return R.drawable.provider_tri;
      case "smartfren":
        return R.drawable.provider_smartfren;
      default:
        return R.drawable.simcard;
    }
  }
}
/*
Telkomsel
0811 ( Halo 10, 11 digit )
0812 ( Halo, Simpati 11, 12 digit )
0813 ( Halo, Simpati 12 digit )
0821 ( Simpati 12 digit )
0822 ( Simpati, Kartu Facebook )
0823 ( AS 12 digit )
0851 ( AS 12 digit )
0852 ( AS 12 digit )
0853 ( AS 12 digit )

		Indosat
0814 ( Indosat M2 Broadband 12 digit )
0815 ( Matriks dan Mentari 11, 12 digit )
0816 ( Matriks dan Mentari 10, 11, 12 digit)
0855 ( Matriks 10 digit )
0856 ( IM3 10(limited), 11, 12 digit )
0857 ( IM3 12 digit )
0858 ( Mentari 12 digit )

		XL
0817 ( Prabayar dan Explor 10, 11, 12 digit )
0818 ( Prabayar dan Explor 10, 11, 12 digit )
0819 ( Prabayar dan Explor 10, 11, 12 digit )
0859 ( Prabayar dan Explor 12 digit )
0877 ( Prabayar dan Explor 12 digit )
0878 ( Prabayar dan Explor 12 digit )


+ Axis
0831
0832
0833
0838


+ Three
0895 ( Prabayar dan Pascabayar 11, 12 digit )
0896 ( Prabayar dan Pascabayar 11, 12 digit )
0897 ( Prabayar dan Pascabayar 11, 12 digit )
0898 ( Prabayar dan Pascabayar 11, 12 digit )
0899 ( Prabayar dan Pascabayar 11, 12 digit )


+ Smartfren
0881
0882
0883
0884
0885
0886
0887
0888
0889

+ Ceria
0828 ( Prabayar dan Pascabayar )
*/

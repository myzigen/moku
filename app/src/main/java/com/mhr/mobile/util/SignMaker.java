package com.mhr.mobile.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignMaker {

  public static String md5(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(input.getBytes());
      BigInteger number = new BigInteger(1, messageDigest);
      String hash = number.toString(16);
      while (hash.length() < 32) {
        hash = "0" + hash;
      }
      return hash;
    } catch (Exception e) {
      return "";
    }
  }

  private static String encrypt(String text) {
    try {
      // Create MD5 Hash
      MessageDigest digest = MessageDigest.getInstance("MD5");
      digest.update(text.getBytes());
      byte[] messageDigest = digest.digest();

      // Create Hex String
      StringBuilder hexString = new StringBuilder();
      for (byte b : messageDigest) {
        StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & b));
        while (h.length() < 2) h.insert(0, "0");
        hexString.append(h);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getSignRefId() {
    return "TX" + System.currentTimeMillis();
  }

  public static String getSign(String username, String key, String extensionSign) {
    return encrypt(username + key + extensionSign);
  }

  public static String SignatureTripay(
      String merchantCode, String merchantRef, int amount, String privateKey) {
    String message = merchantCode + merchantRef + amount;
    return sha256_HMAC(privateKey, message);
  }

  // merchantCode.$merchantRef.$amount, $privateKey

  // Ipaymu
  public static String generateSignature(String jsonBody, String va, String apikey) {
    try {
      // 1. Hash JSON body menggunakan SHA-256
      String requestBody = sha256(jsonBody).toLowerCase();

      // 2. Format StringToSign
      String stringToSign = "POST:" + va + ":" + requestBody + ":" + apikey;

      // 3. Gunakan HMAC-SHA256 dengan apiKey sebagai secret
      return sha256_HMAC(apikey, stringToSign);

    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  // Fungsi untuk membuat hash SHA-256 dari JSON body
  private static String sha256(String data) throws Exception {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hash = digest.digest(data.getBytes("UTF-8"));
    return bytesToHex(hash);
  }

  // Fungsi untuk membuat HMAC-SHA256
  private static String sha256_HMAC(String secret, String message) {
    String hash = "";
    try {
      Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
      sha256_HMAC.init(secret_key);
      byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
      hash = bytesToHex(bytes);
    } catch (Exception e) {

    }
    return hash;
  }

  // Fungsi untuk mengonversi byte array ke hexadecimal
  private static String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder();
    for (byte b : hash) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) hexString.append('0');
      hexString.append(hex);
    }
    return hexString.toString().toLowerCase();
  }
}

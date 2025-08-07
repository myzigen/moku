package com.mhr.mobile.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import java.util.ArrayList;

public class ContactUtils {

  public static ArrayList<String> getAllContacts(Context context) {
    ArrayList<String> contactList = new ArrayList<>();
    ContentResolver cr = context.getContentResolver();

    Cursor cursor =
        cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

    if (cursor != null) {
      while (cursor.moveToNext()) {
        String nama =
            cursor.getString(
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String nomor =
            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        contactList.add(nama + " : " + nomor);
      }
      cursor.close();
    }

    return contactList;
  }

  public static ArrayList<String> filterContacts(ArrayList<String> allContacts, String query) {
    ArrayList<String> filtered = new ArrayList<>();
    for (String contact : allContacts) {
      if (contact.toLowerCase().contains(query.toLowerCase())) {
        filtered.add(contact);
      }
    }
    return filtered;
  }

  public static String highlightQuery(String text, String query) {
    try {
      String lowerText = text.toLowerCase();
      String lowerQuery = query.toLowerCase();
      int start = lowerText.indexOf(lowerQuery);
      if (start >= 0) {
        int end = start + query.length();
        return text.substring(0, start)
            + "<font color='#2196F3'><b>"
            + text.substring(start, end)
            + "</b></font>"
            + text.substring(end);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return text;
  }
}

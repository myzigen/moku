package com.mhr.mobile.loader;

import android.app.Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mhr.mobile.model.Bank;
import java.util.ArrayList;
import java.util.List;

public class QiosDatabase {

  private Activity activity;
  private DatabaseReference db;

  public QiosDatabase(Activity activity) {
    this.activity = activity;
  }

  public void setDatabaseName(String dbName) {
    db = FirebaseDatabase.getInstance().getReference(dbName);
  }

  public void setLoadDataBank(DatabaseBank callback) {
	callback.onStart();
    db.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            List<Bank.BankHeader> recyclerItems = new ArrayList<>();
            List<Bank> qris = new ArrayList<>();
            List<Bank> virtualAccounts = new ArrayList<>();
            List<Bank> transfer = new ArrayList<>();
            List<Bank> eWallets = new ArrayList<>();

            // Pisahkan data berdasarkan tipe
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              Bank bank = snapshot.getValue(Bank.class);
              if (bank != null) {
                if ("Qris".equals(bank.getType())) {
                  qris.add(bank);
                } else if ("VA".equals(bank.getType())) {
                  virtualAccounts.add(bank);
                } else if ("Transfer".equals(bank.getType())) {
                  transfer.add(bank);
                } else if ("E-Wallet".equals(bank.getType())) {
                  eWallets.add(bank);
                }
              }
            }

            // Tambahkan header dan item untuk Qris
            if (!qris.isEmpty()) {
              recyclerItems.add(new Bank.BankHeader(Bank.BankHeader.TYPE_HEADER, "Qris", null));
              for (Bank bank : qris) {
                recyclerItems.add(new Bank.BankHeader(Bank.BankHeader.TYPE_ITEM, null, bank));
              }
            }

            // Tambahkan header dan item untuk Virtual Account
            if (!virtualAccounts.isEmpty()) {
              recyclerItems.add(
                  new Bank.BankHeader(Bank.BankHeader.TYPE_HEADER, "Virtual Account", null));
              for (Bank bank : virtualAccounts) {
                recyclerItems.add(new Bank.BankHeader(Bank.BankHeader.TYPE_ITEM, null, bank));
              }
            }

            // Tambahkan header dan item untuk Transfer

            if (!transfer.isEmpty()) {
              recyclerItems.add(new Bank.BankHeader(Bank.BankHeader.TYPE_HEADER, "Transfer", null));
              for (Bank bank : transfer) {
                recyclerItems.add(new Bank.BankHeader(Bank.BankHeader.TYPE_ITEM, null, bank));
              }
            }

            // Tambahkan header dan item untuk E-Wallet
            if (!eWallets.isEmpty()) {
              recyclerItems.add(new Bank.BankHeader(Bank.BankHeader.TYPE_HEADER, "E-Wallet", null));
              for (Bank bank : eWallets) {
                recyclerItems.add(new Bank.BankHeader(Bank.BankHeader.TYPE_ITEM, null, bank));
              }
            }
            callback.onDataChange(recyclerItems);
          }

          @Override
          public void onCancelled(DatabaseError error) {
            callback.onFailure(error.toString());
          }
        });
  }

  public interface DatabaseBank {
    public void onStart();

    public void onDataChange(List<Bank.BankHeader> bank);

    public void onFailure(String error);
  }
}

package com.mhr.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.R;
import com.mhr.mobile.model.Kontak;
import java.util.List;

public class KontakAdapter extends RecyclerView.Adapter<KontakAdapter.ContactViewHolder> {

  private List<Kontak> contactList;

  public KontakAdapter(List<Kontak> contactList) {
    this.contactList = contactList;
  }

  @Override
  public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kontak, parent, false);
    return new ContactViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ContactViewHolder holder, int position) {
    Kontak contact = contactList.get(position);
    holder.contactName.setText(contact.getName());
    // Set contact image if available (can be a Uri or drawable)
    // holder.contactImage.setImageURI(Uri.parse(contact.getPhotoUri()));
  }

  @Override
  public int getItemCount() {
    return contactList.size();
  }

  public static class ContactViewHolder extends RecyclerView.ViewHolder {
    public TextView contactName;
    public ImageView contactImage;

    public ContactViewHolder(View view) {
      super(view);
      contactName = view.findViewById(R.id.contactName);
      contactImage = view.findViewById(R.id.contactImage);
    }
  }
}

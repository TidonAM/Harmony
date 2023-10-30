package com.bsit212.harmony;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactsRecyclerView extends RecyclerView.Adapter<ContactsVH> {

    List<ItemData> items;

    public ContactsRecyclerView(List<ItemData> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ContactsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_recylcerrow, parent, false);
        return new ContactsVH(view).linkAdapter(this);
    }

    public void setItems(List<ItemData> items) {
        this.items = items;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsVH holder, int position) {
        ItemData item = items.get(position);

        // Set the data from the ItemData object
        holder.textTop.setText(item.getUsername());
        holder.textBottom.setText(item.getEmail());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class ContactsVH extends RecyclerView.ViewHolder{

    TextView textTop;
    TextView textBottom;
    private ContactsRecyclerView contactsRecyclerView;
    public ContactsVH(@NonNull View itemView) {
        super(itemView);
        textTop = itemView.findViewById(R.id.search_texttop);
        textBottom = itemView.findViewById(R.id.search_textbottom);
    }

    public ContactsVH linkAdapter(ContactsRecyclerView contactsRecyclerView){
        this.contactsRecyclerView = contactsRecyclerView;
        return this;
    }
}
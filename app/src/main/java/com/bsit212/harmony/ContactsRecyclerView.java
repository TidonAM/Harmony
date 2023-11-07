package com.bsit212.harmony;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bsit212.harmony.models.ContactsModel;
import com.bsit212.harmony.models.UserModel;

import java.util.List;

public class ContactsRecyclerView extends RecyclerView.Adapter<ContactsVH> {
    List<ContactsModel> items;
    private OnItemClickListener clickListener;

    public ContactsRecyclerView(List<ContactsModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ContactsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_recylcerrow, parent, false);
        return new ContactsVH(view,clickListener).linkAdapter(this);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String toptext, String bottomtext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsVH holder, int position) {
        ContactsModel item = items.get(position);

        // Set the data from the ItemData object
        holder.textTop.setText(item.getUser().getUsername());
        if (item.getLastMessage() != null){
            holder.textBottom.setText(item.getLastMessage().getText());
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

class ContactsVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView textTop;
    TextView textBottom;
    private ContactsRecyclerView contactsRecyclerView;
    private ContactsRecyclerView.OnItemClickListener clickListener;
    public ContactsVH(@NonNull View itemView, ContactsRecyclerView.OnItemClickListener clickListener) {
        super(itemView);
        textTop = itemView.findViewById(R.id.search_texttop);
        textBottom = itemView.findViewById(R.id.search_textbottom);

        this.clickListener = clickListener;
        itemView.setOnClickListener(this);
    }

    public void setClickListener(ContactsRecyclerView.OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                clickListener.onItemClick(position,textTop.getText().toString(),textBottom.getText().toString());
            }
        }
    }

    public ContactsVH linkAdapter(ContactsRecyclerView contactsRecyclerView){
        this.contactsRecyclerView = contactsRecyclerView;
        return this;
    }
}

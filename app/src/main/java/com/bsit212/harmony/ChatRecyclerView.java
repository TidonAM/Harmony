package com.bsit212.harmony;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatRecyclerView extends RecyclerView.Adapter<ChatVH>{

    List<String> items;

    public ChatRecyclerView(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_send, parent, false);
        return new ChatVH(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatVH holder, int position) {
        holder.textView.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class ChatVH extends RecyclerView.ViewHolder{

    TextView textView;
    private ChatRecyclerView chatRecyclerView;
    public ChatVH(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tv_send);
    }

    public ChatVH linkAdapter(ChatRecyclerView chatRecyclerView){
        this.chatRecyclerView = chatRecyclerView;
        return this;
    }
}
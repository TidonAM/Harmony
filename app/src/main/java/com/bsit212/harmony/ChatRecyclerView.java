package com.bsit212.harmony;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerView extends FirestoreRecyclerAdapter<MessageModel, ChatRecyclerView.ChatVH> {

    Context context;

    public ChatRecyclerView(@NonNull FirestoreRecyclerOptions<MessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_send, parent, false);
        return new ChatVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatVH holder, int position, @NonNull MessageModel message) {
        if (message.getSender().equals(MainActivity.getCurrentUIDStr())) {
            holder.tvRcv.setVisibility(View.GONE);
            holder.layoutRcv.setVisibility(View.GONE);
            holder.tvSend.setText(message.getText());
        } else {
            holder.tvSend.setVisibility(View.GONE);
            holder.layoutSend.setVisibility(View.GONE);
            holder.tvRcv.setText(message.getText());
        }
    }

    class ChatVH extends RecyclerView.ViewHolder {
        TextView tvSend;
        TextView tvRcv;
        RelativeLayout layoutSend;
        RelativeLayout layoutRcv;

        public ChatVH(@NonNull View itemView) {
            super(itemView);
            tvSend = itemView.findViewById(R.id.tv_send);
            tvRcv = itemView.findViewById(R.id.tv_rcv);
            layoutSend = itemView.findViewById(R.id.layout_send);
            layoutRcv = itemView.findViewById(R.id.layout_rcv);
        }
    }
}

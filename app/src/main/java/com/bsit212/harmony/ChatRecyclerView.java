package com.bsit212.harmony;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bsit212.harmony.cmd.FirebaseCmd;
import com.bsit212.harmony.models.*;
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
        if (message.getSender().equals(FirebaseCmd.currentUserId())) {
            holder.tvSend.setText(message.getText());
            holder.tvSend.setVisibility(View.VISIBLE);
            holder.layoutSend.setVisibility(View.VISIBLE);
            holder.tvRcv.setVisibility(View.GONE);
            holder.layoutRcv.setVisibility(View.GONE);
        } else {
            holder.tvRcv.setText(message.getText());
            holder.tvRcv.setVisibility(View.VISIBLE);
            holder.layoutRcv.setVisibility(View.VISIBLE);
            holder.tvSend.setVisibility(View.GONE);
            holder.layoutSend.setVisibility(View.GONE);
        }

        if (position == getItemCount() - 1){
            holder.layoutMain.setPadding(0,0,0,20);
        } else if (position == 0) {
            holder.layoutMain.setPadding(0,0,0,0);
        } else {
            holder.layoutMain.setPadding(0,0,0,0);
        }

        if(position != 0 && position != getItemCount()-1){

        }
    }

    class ChatVH extends RecyclerView.ViewHolder {
        TextView tvSend;
        TextView tvRcv;
        RelativeLayout layoutSend;
        CardView layoutRcv;
        RelativeLayout layoutMain;

        public ChatVH(@NonNull View itemView) {
            super(itemView);
            tvSend = itemView.findViewById(R.id.tv_send);
            tvRcv = itemView.findViewById(R.id.tv_rcv);
            layoutSend = itemView.findViewById(R.id.layout_send);
            layoutRcv = itemView.findViewById(R.id.layout_rcv);
            layoutMain  = itemView.findViewById(R.id.layout_main);
        }
    }
}

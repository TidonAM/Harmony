package com.bsit212.harmony;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

public class MessageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MainActivity mainActivity;
    String chatroomId;
    TextView tvUsername;
    RecyclerView recyclerView;
    ChatRecyclerView chatRecyclerView;
    UserModel otherUserModel;
    int counter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message,container,false);
        mainActivity = (MainActivity) getActivity();

        otherUserModel = mainActivity.otherUserModel;
        chatroomId = FirebaseCmd.getChatroomId(FirebaseCmd.currentUserId(),otherUserModel.getUid());
        tvUsername = view.findViewById(R.id.message_tv_username);

        if (otherUserModel == null) {
            Toast.makeText(getActivity(),"Invalid User",Toast.LENGTH_SHORT);
            Log.e("yowell","Invalid User");
            mainActivity.launchFragment(MainActivity.launchFragment.contacts);
        } else {
            tvUsername.setText(otherUserModel.getUsername());
            mainActivity.getOrCreateChatroom(otherUserModel.getUid(), new MainActivity.MessageCallback() {
                @Override
                public void onMessagesReceived(List<MessageModel> messages, Query messagesCollectionSorted, CollectionReference messagesCollection) {
                    List<MessageModel> items = new LinkedList<>();
                    items.addAll(messages);
                    Log.i("yowell", items.toString());
                    // Initialize your RecyclerView and adapter here
                    EditText etType = view.findViewById(R.id.et_type);
                    recyclerView = view.findViewById(R.id.message_recyclerview);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    linearLayoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    Query query = messagesCollectionSorted;
                    FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                            .setQuery(query, MessageModel.class)
                            .build();
                    ChatRecyclerView chatRecyclerView = new ChatRecyclerView(options, getContext());
                    recyclerView.setAdapter(chatRecyclerView);
                    chatRecyclerView.startListening();

                    chatRecyclerView.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeInserted(int positionStart, int itemCount) {
                            super.onItemRangeInserted(positionStart, itemCount);
                            int endPosition = positionStart + itemCount - 1;
                            recyclerView.smoothScrollToPosition(endPosition);
                        }
                    });

                    view.findViewById(R.id.img_send).setOnClickListener(view2 -> {
                        Log.d("yowell", "Send button clicked");
                        String typed = etType.getText().toString();
                        typed = typed.trim();
                        if (typed.isEmpty()) {
                            etType.setText("");
                        } else {
                            MessageModel message = new MessageModel();
                            message.setSender(mainActivity.getCurrentUIDStr());
                            message.setText(typed);
                            message.setTimestamp(Timestamp.now());
                            Map<String, Object> messageData = new HashMap<>();
                            messageData.put("sender", mainActivity.getCurrentUIDStr());
                            messageData.put("text", typed);
                            messageData.put("timestamp", Timestamp.now());

                            messagesCollection.add(messageData)
                                    .addOnSuccessListener(documentReference -> {
                                        items.add(message);
                                        Log.d("yowell", "Message Sent ");
//                                                chatRecyclerView.notifyItemInserted(items.size() - 1);
//                                                recyclerView.scrollToPosition(items.size() - 1);
                                        etType.setText(""); // Clear the input field
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the case where the message sending failed
                                        Log.d("yowell", "Error sending message: " + e);
                                    });
                        }
                    });
                }
                @Override
                public void onMessageFetchFailed() {
                    Log.i("yowell", "onMessageFetchFailed()");
                }
            });
        }

        EditText etType = view.findViewById(R.id.et_type);

        view.findViewById(R.id.img_call).setOnClickListener(view2 -> {
            if (mainActivity != null) {
                mainActivity.launchFragment(MainActivity.launchFragment.call);
            }
        });

        return view;
    }

    public MessageFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

}
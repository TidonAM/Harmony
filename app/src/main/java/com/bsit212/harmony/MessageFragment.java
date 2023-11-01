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

import com.bsit212.harmony.MainActivity.Message;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;

public class MessageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String otherUsername;
    TextView tvUsername;
    RecyclerView recyclerView;
    ChatRecyclerView chatRecyclerView;
    int counter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message,container,false);

        MainActivity mainActivity = (MainActivity) getActivity();

//        int statusBarHeight = mainActivity.getStatusBarHeight();
//        View layout = view.findViewById(R.id.toolbar); // Replace with your layout's ID
//        layout.setPadding(20, statusBarHeight+80, 0, 30);
        tvUsername = view.findViewById(R.id.message_tv_username);
        otherUsername = mainActivity.otherUsername;

        if (otherUsername == null) {
            Toast.makeText(getActivity(),"Invalid Username",Toast.LENGTH_SHORT);
            mainActivity.launchFragment(MainActivity.launchFragment.contacts);
        } else {
            tvUsername.setText(otherUsername);
        }

        EditText etType = view.findViewById(R.id.et_type);


        mainActivity.fetchOtherUser(otherUsername, new MainActivity.FetchUserCallback() {
            @Override
            public void onUserFetched(MainActivity.userClass user) {
                if (user == null) {
                    Toast.makeText(getActivity(), "Invalid User", Toast.LENGTH_SHORT);
                    mainActivity.launchFragment(MainActivity.launchFragment.contacts);
                } else {
                    tvUsername.setText(otherUsername);
                    Log.i("yowell", "onUserFetched(): " + user.getUid());

                    mainActivity.getOrCreateChatroom(user.getUid(), new MainActivity.MessageCallback() {
                        List<Message> items = new LinkedList<>();
                        @Override
                        public void onMessagesReceived(List<Message> messages, CollectionReference messagesCollection) {
                            for (Message message : messages) {
                                Log.i("yowell", "for Message message" + message.getText());
                            }
                            items.clear();
                            items.addAll(messages);
                            Log.i("yowell", items.toString());
                            EditText etType = view.findViewById(R.id.et_type);
                            recyclerView = view.findViewById(R.id.message_recyclerview);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            linearLayoutManager.setStackFromEnd(true);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            chatRecyclerView = new ChatRecyclerView(items);
                            recyclerView.setAdapter(chatRecyclerView);

                            view.findViewById(R.id.img_send).setOnClickListener(view2 -> {
                                String typed = etType.getText().toString();
                                typed = typed.trim();
                                Message message = new Message();
                                message.setSender(mainActivity.getCurrentUIDStr());
                                message.setText(typed);
                                message.setTimestamp(Timestamp.now());
                                Map<String, Object> messageData = new HashMap<>();
                                messageData.put("sender", mainActivity.getCurrentUIDStr());
                                messageData.put("text", typed);
                                messageData.put("timestamp", Timestamp.now());
                                if (typed.isEmpty()) {
                                    etType.setText("");
                                } else {
//                                    counter++;
//                                    chatRecyclerView.notifyItemInserted(items.size()-1);
//                                    recyclerView.scrollToPosition(items.size()-1);
//                                    etType.setText("");
                                    messagesCollection.add(messageData)
                                            .addOnSuccessListener(documentReference -> {
                                                items.add(message);
                                                Toast.makeText(getActivity(), "message sent", Toast.LENGTH_SHORT).show();
                                                chatRecyclerView.notifyItemInserted(items.size()-1);
                                                counter++;
                                                etType.setText(""); // Clear the input field
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle the case where the message sending failed
                                                Log.e("Firestore", "Error sending message: " + e);
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
            }
            @Override
            public void onUserFetchFailed() {
                // Handle the case where user data fetch failed
                Toast.makeText(getActivity(), "Failed to fetch user data", Toast.LENGTH_SHORT);
                mainActivity.launchFragment(MainActivity.launchFragment.contacts);
            }
        });

        view.findViewById(R.id.img_call).setOnClickListener(view2 -> {
            if (mainActivity != null) {
                mainActivity.MessagetoCall();
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
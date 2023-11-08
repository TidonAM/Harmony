package com.bsit212.harmony;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bsit212.harmony.cmd.FirebaseCmd;
import com.bsit212.harmony.models.MessageModel;
import com.bsit212.harmony.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

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
    View view;
    private int limit = 30;
    static Query messagesCollectionSorted;
    static CollectionReference messagesCollection;

    List<MessageModel> items;
    int counter = 0;

    public void setupRecycler(View view){
//        Query query = FirebaseCmd.getChatroomReference(chatroomId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        items = new LinkedList<>();
        view = inflater.inflate(R.layout.fragment_message,container,false);
        mainActivity = (MainActivity) getActivity();
        otherUserModel = mainActivity.otherUserModel;
        visibleProgress(true);

        if (otherUserModel == null) {
            Toast.makeText(getActivity(),"Invalid User",Toast.LENGTH_SHORT);
            Log.e("yowell","Invalid Other User");
            mainActivity.launchFragment(MainActivity.launchFragment.contacts);
        } else {
            Log.e("yowell","messageFragment(): "+otherUserModel.getUid());
            chatroomId = FirebaseCmd.getChatroomId(FirebaseCmd.currentUserId(),otherUserModel.getUid());
            tvUsername = view.findViewById(R.id.message_tv_username);
            recyclerView = view.findViewById(R.id.message_recyclerview);
            tvUsername.setText(otherUserModel.getUsername());
            visibleProgress(true);

            mainActivity.getOrCreateChatroom(otherUserModel.getUid(), new MainActivity.MessageCallback() {
                @Override
                public void onMessagesReceived(List<MessageModel> messages, Query messagesCollectionSorted, CollectionReference messagesCollection) {
                    handleMessagesReceived(messagesCollectionSorted);
//                    handleMessagesReceived(messages, messagesCollectionSorted, messagesCollection);
                    MessageFragment.messagesCollectionSorted = messagesCollectionSorted;
                    MessageFragment.messagesCollection = messagesCollection;
                    initRecyclerView(view);
                    initSendButton(view);
                    visibleProgress(false);
                }
                @Override
                public void onMessageFetchFailed() {
                    Log.e("yowell", "onMessageFetchFailed()");
                }
            });
        }
        view.findViewById(R.id.img_call).setOnClickListener(view2 -> {
            if (mainActivity != null) {
                try {
                    URL serverUrl = new URL("https://meet.mayfirst.org");
                    JitsiMeetConferenceOptions defaultOptions = new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverUrl)
                            .setFeatureFlag("welcomepage.enabled", false)
                            .build();
                    JitsiMeet.setDefaultConferenceOptions(defaultOptions);
//                    mainActivity.launchFragment(MainActivity.launchFragment.call);
                    createCall(chatroomId);
                } catch (MalformedURLException e){

                }

            }
        });

        return view;
    }

    private void createCall(String chatroomId){
        JitsiMeetUserInfo userInfo = new JitsiMeetUserInfo();
        userInfo.setDisplayName(mainActivity.currentUserModel.getUsername());
        JitsiMeetConferenceOptions roomOptions = new JitsiMeetConferenceOptions.Builder()
                .setRoom(chatroomId)
                .setFeatureFlag("welcomepage.enabled", false)
                .setFeatureFlag("meeting-name.enabled", false)
                .setFeatureFlag("add-people.enabled", false)
                .setFeatureFlag("chat.enabled", false)
                .setFeatureFlag("prejoinpage.enabled",false)
                .setFeatureFlag("lobby-mode.enabled", false)
                .setFeatureFlag("help.enabled",false)
                .setFeatureFlag("invite.enabled",false)
                .setFeatureFlag("raise-hand.enabled",false)
                .setFeatureFlag("reactions.enabled",false)
                .setFeatureFlag("kick-out.enabled",false)
                .setUserInfo(userInfo)
                .build();
        JitsiMeetActivity.launch(getContext(), roomOptions);
    }
    private void initRecyclerView(View view) {
        Query query = messagesCollectionSorted.limit(limit);
        FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(query, MessageModel.class)
                .build();
        chatRecyclerView = new ChatRecyclerView(options, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initSendButton(View view) {
        EditText etType = view.findViewById(R.id.et_type);
        view.findViewById(R.id.img_send).setOnClickListener(view2 -> sendMessage(etType, view));
    }

    private void buildRecyclerOptions(Query messagesCollectionSorted){
        limit += 30;
        Query query = messagesCollectionSorted.limitToLast(limit);
        query.get().addOnCompleteListener(task -> {
            Log.d("yowell", "add on complete listener");
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Log.d("yowell", "Document ID: " + document.getTimestamp("timestamp").toDate());
                    // Log other document details as needed
                }
            } else {
                Log.e("yowell", "Error executing the query: " + task.getException());
            }
        });
        FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(query, MessageModel.class)
                .build();
        chatRecyclerView.updateOptions(options);
        Log.d("yowell", "Options updated. New limit: " + limit);
        Log.d("yowell", "Query: " + query.toString());
    };

    private void visibleProgress(boolean bool){
        RelativeLayout progresslayout = view.findViewById(R.id.message_layout_progress);
        if (bool == true){
            progresslayout.setVisibility(View.VISIBLE);
        } else if (bool == false){
            progresslayout.setVisibility(View.GONE);
        }

    }

    private void handleMessagesReceived(Query messagesCollectionSorted) {

//        Query query = messagesCollectionSorted.limitToLast(limit);
        Query query = messagesCollectionSorted;
        FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(query, MessageModel.class)
                .build();

        chatRecyclerView = new ChatRecyclerView(options, getContext());
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(-1)) {
                    Log.d("yowell", "Scrolled to top");
//                    buildRecyclerOptions(messagesCollectionSorted);
                }
            }
        });
    }

    private void sendMessage(EditText etType, View view) {
        String typed = etType.getText().toString();
        typed = typed.trim();
        if (typed.isEmpty()) {
            etType.setText("");
        } else {
            MessageModel message = new MessageModel();
            Log.e("yowell", messagesCollection.toString());
            message.setSender(FirebaseCmd.currentUserId());
            message.setText(typed);
            message.setTimestamp(Timestamp.now());
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("sender", FirebaseCmd.currentUserId());
            messageData.put("text", typed);
            messageData.put("timestamp", Timestamp.now());

            messagesCollection.add(messageData)
                    .addOnSuccessListener(documentReference -> {
                        etType.setText("");
                        items.add(message);
                        Log.d("yowell", "Message Sent ");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("yowell", "Error sending message: " + e);
                    });

        }
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